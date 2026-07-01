package feed.domain

import androidx.paging.PagingData
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import feed.domain.model.CoinListing
import feed.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    suspend fun storeFeed(limit: Int = 6): EmptyNetworkResult<NetworkError>
    suspend fun getLatestPosts(limit: Int = 6): Flow<List<Post>>
    fun getFeed(limit: Int = 20): Flow<PagingData<Post>>
    suspend fun storeCoinListings(limit: Int = 10): EmptyNetworkResult<NetworkError>
    suspend fun getLatestCoinListings(limit: Int = 10): Flow<List<CoinListing>>
    fun getCoinListings(limit: Int = 20): Flow<PagingData<CoinListing>>
}