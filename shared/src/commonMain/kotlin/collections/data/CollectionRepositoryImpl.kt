package collections.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.data.local.AppDatabase
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.asEmptyDataNetworkResult
import app.domain.model.map
import collections.data.local.dao.CoinDetailsDao
import collections.data.local.dao.CollectionStatsDao
import collections.data.remote.CoinPagingSource
import collections.data.remote.dto.CoinDto
import collections.data.remote.dto.CollectionStatsDto
import collections.data.remote.mapper.toAiAnalysisEntity
import collections.data.remote.mapper.toCatalogueNumberEntities
import collections.data.remote.mapper.toCoinDataEntity
import collections.data.remote.mapper.toCoinDetailsEntity
import collections.data.remote.mapper.toCollectionHighlightsEntities
import collections.data.remote.mapper.toCollectionStatsEntity
import collections.domain.CollectionRepository
import collections.domain.mapper.toCoinDetails
import collections.domain.mapper.toCollectionStats
import collections.domain.model.CoinDetails
import collections.domain.model.CollectionStats
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepositoryImpl(
    private val httpClient: HttpClient,
    db: AppDatabase
) : CollectionRepository {
    private val coinDetailsDao: CoinDetailsDao = db.coinDetailsDao()
    private val collectionStatsDao: CollectionStatsDao = db.collectionStatsDao()

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

    override fun getCoins(limit: Int) =
        Pager(
            config = PagingConfig(
                pageSize = limit,
                initialLoadSize = limit,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { CoinPagingSource(httpClient, limit) }
        ).flow

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
}