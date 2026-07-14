package collections.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.data.local.AppDatabase
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.NetworkResult
import app.domain.model.asEmptyDataNetworkResult
import app.domain.model.map
import collections.data.local.dao.CoinDao
import collections.data.local.dao.CoinDetailsDao
import collections.data.local.dao.CoinPagingStateDao
import collections.data.local.dao.CoinSetDao
import collections.data.local.dao.CollectionStatsDao
import collections.data.remote.CoinRemoteMediator
import collections.data.remote.dto.BulkDeleteCoinsRequest
import collections.data.remote.dto.BulkDeleteResponse
import collections.data.remote.dto.BulkDeleteSetsRequest
import collections.data.remote.dto.CoinDto
import collections.data.remote.dto.CoinSetDto
import collections.data.remote.dto.CollectionStatsDto
import collections.data.remote.dto.CreateCoinSetRequest
import collections.data.remote.dto.ModifySetCoinsRequest
import collections.data.remote.mapper.toAiAnalysisEntity
import collections.data.remote.mapper.toCatalogueNumberEntities
import collections.data.remote.mapper.toCoin
import collections.data.remote.mapper.toCoinDataEntity
import collections.data.remote.mapper.toCoinDetailsEntity
import collections.data.remote.mapper.toCoinSet
import collections.data.remote.mapper.toCoinSetEntities
import collections.data.remote.mapper.toCollectionHighlightsEntities
import collections.data.remote.mapper.toCollectionStatsEntity
import collections.domain.CollectionRepository
import collections.domain.mapper.toCoinDetails
import collections.domain.mapper.toCollectionStats
import collections.domain.model.Coin
import collections.domain.model.CoinDetails
import collections.domain.model.CoinSet
import collections.domain.model.CollectionStats
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepositoryImpl(
    private val httpClient: HttpClient,
    private val db: AppDatabase
) : CollectionRepository {
    private val coinDetailsDao: CoinDetailsDao = db.coinDetailsDao()
    private val collectionStatsDao: CollectionStatsDao = db.collectionStatsDao()
    private val coinSetDao: CoinSetDao = db.coinSetDao()
    private val coinDao: CoinDao = db.coinDao()
    private val coinPagingStateDao: CoinPagingStateDao = db.coinPagingStateDao()

    override suspend fun storeCollectionStats(): EmptyNetworkResult<NetworkError> {
        return safeCall<CollectionStatsDto> {
            httpClient.get(urlString = constructUrl("/coins/stats"))
        }.map { dto ->
            collectionStatsDao.upsertCollectionStats(
                stats = dto.toCollectionStatsEntity(),
                highlights = dto.toCollectionHighlightsEntities()
            )
        }.asEmptyDataNetworkResult()
    }

    override fun getCollectionStats(): Flow<CollectionStats?> =
        collectionStatsDao.getCollectionStats().map { it?.toCollectionStats() }

    @OptIn(ExperimentalPagingApi::class)
    override fun getCoins(limit: Int): Flow<PagingData<Coin>> =
        Pager(
            config = PagingConfig(
                pageSize = limit,
                initialLoadSize = limit,
                enablePlaceholders = false,
            ),
            remoteMediator = CoinRemoteMediator(httpClient, db, limit),
            pagingSourceFactory = { coinDao.pagingSource(setId = null) }
        ).flow.map { pagingData -> pagingData.map { it.toCoin() } }

    override suspend fun storeCoinDetails(id: String): EmptyNetworkResult<NetworkError> {
        return safeCall<CoinDto> {
            httpClient.get(urlString = constructUrl("/coins/$id"))
        }.map { dto ->
            coinDetailsDao.upsertCoinDetails(
                coin = dto.toCoinDetailsEntity(),
                coinData = dto.toCoinDataEntity(),
                aiAnalysis = dto.toAiAnalysisEntity(),
                catalogueNumbers = dto.toCatalogueNumberEntities()
            )
        }.asEmptyDataNetworkResult()
    }

    override fun getCoinDetails(id: String): Flow<CoinDetails?> =
        coinDetailsDao.getByIdWithRelations(id).map { it?.toCoinDetails() }

    override suspend fun storeSets(): EmptyNetworkResult<NetworkError> {
        return safeCall<List<CoinSetDto>> {
            httpClient.get(urlString = constructUrl("/sets"))
        }.map { dtos ->
            coinSetDao.replaceAll(dtos.toCoinSetEntities())
        }.asEmptyDataNetworkResult()
    }

    override fun getSets(): Flow<List<CoinSet>> =
        coinSetDao.getAllSets().map { entities -> entities.map { it.toCoinSet() } }

    override suspend fun createSet(
        name: String,
        description: String?
    ): EmptyNetworkResult<NetworkError> {
        return safeCall<CoinSetDto> {
            httpClient.post(urlString = constructUrl("/sets")) {
                setBody(CreateCoinSetRequest(name = name, description = description))
            }
        }.map {
            storeSets()
        }.asEmptyDataNetworkResult()
    }

    override suspend fun deleteCoins(coinIds: List<String>): NetworkResult<BulkDeleteResponse, NetworkError> {
        return safeCall<BulkDeleteResponse> {
            httpClient.delete(urlString = constructUrl("/coins")) {
                setBody(BulkDeleteCoinsRequest(coinIds = coinIds))
            }
        }.map { response ->
            coinDao.deleteByIds(coinIds)
            storeCollectionStats()
            storeSets()
            response
        }
    }

    override suspend fun deleteSets(setIds: List<String>): NetworkResult<BulkDeleteResponse, NetworkError> {
        return safeCall<BulkDeleteResponse> {
            httpClient.delete(urlString = constructUrl("/sets")) {
                setBody(BulkDeleteSetsRequest(setIds = setIds))
            }
        }.map { response ->
            storeSets()
            storeCollectionStats()
            response
        }
    }

    override suspend fun moveCoinsToSet(
        targetSetId: String,
        coinIds: List<String>
    ): EmptyNetworkResult<NetworkError> {
        return safeCall<CoinSetDto> {
            httpClient.post(urlString = constructUrl("/sets/$targetSetId/coins")) {
                setBody(ModifySetCoinsRequest(coinIds = coinIds))
            }
        }.map {
            coinDao.setSetIdForIds(coinIds, targetSetId)
            storeSets()
            storeCollectionStats()
        }.asEmptyDataNetworkResult()
    }
}