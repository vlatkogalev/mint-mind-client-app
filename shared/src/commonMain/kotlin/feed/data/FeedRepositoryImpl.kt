package feed.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.data.local.AppDatabase
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.asEmptyDataNetworkResult
import app.domain.model.map
import feed.data.remote.CoinListingsPagingSource
import feed.data.remote.FeedPagingSource
import feed.data.remote.dto.MarketplaceListingsDto
import feed.data.remote.dto.PostsDto
import feed.data.remote.mapper.toCoinListingEntities
import feed.data.remote.mapper.toPostEntities
import feed.domain.FeedRepository
import feed.domain.mapper.toCoinListingItems
import feed.domain.mapper.toPosts
import feed.domain.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedRepositoryImpl(
    private val httpClient: HttpClient,
    db: AppDatabase
) : FeedRepository {
    private val postDao = db.postDao()
    private val coinListingDao = db.coinListingDao()

    override suspend fun storeFeed(limit: Int): EmptyNetworkResult<NetworkError> {
        return safeCall<PostsDto> {
            httpClient.get(urlString = constructUrl("/news")) {
                parameter("limit", limit)
            }
        }.map { dto ->
            postDao.insertAll(dto.toPostEntities())
        }.asEmptyDataNetworkResult()
    }

    override suspend fun getLatestPosts(limit: Int) = postDao.getLatestPosts(limit).map {
        it.toPosts()
    }

    override fun getFeed(limit: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                initialLoadSize = limit,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { FeedPagingSource(httpClient, limit) }
        ).flow
    }

    override suspend fun storeCoinListings(limit: Int): EmptyNetworkResult<NetworkError> {
        return safeCall<MarketplaceListingsDto> {
            httpClient.get(urlString = constructUrl("/marketplace/listings")) {
                parameter("limit", limit)
            }
        }.map {
            val coinListings = it.toCoinListingEntities()
            if (coinListings.isNotEmpty()) {
                coinListingDao.insertAll(coinListings)
            }
        }
    }

    override suspend fun getLatestCoinListings(limit: Int) =
        coinListingDao.getLatestCoinListings(limit).map {
            it.toCoinListingItems()
        }

    override fun getCoinListings(limit: Int) =
        Pager(
            config = PagingConfig(
                pageSize = limit,
                initialLoadSize = limit,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { CoinListingsPagingSource(httpClient, limit) }
        ).flow
}