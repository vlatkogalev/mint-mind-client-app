package collections.domain

import androidx.paging.PagingData
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import collections.domain.model.Coin
import collections.domain.model.CoinDetails
import collections.domain.model.CollectionStats
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    suspend fun storeCollectionStats(): EmptyNetworkResult<NetworkError>
    fun getCollectionStats(): Flow<CollectionStats?>
    fun getCoins(limit: Int = 20): Flow<PagingData<Coin>>
    suspend fun storeCoinDetails(id: String): EmptyNetworkResult<NetworkError>
    fun getCoinDetails(id: String): Flow<CoinDetails?>
}