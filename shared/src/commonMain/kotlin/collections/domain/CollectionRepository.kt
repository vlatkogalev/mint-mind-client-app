package collections.domain

import androidx.paging.PagingData
import app.domain.NetworkError
import app.domain.model.EmptyNetworkResult
import app.domain.model.NetworkResult
import collections.data.remote.dto.BulkDeleteResponse
import collections.data.remote.dto.SaveToCollectionRequest
import collections.domain.model.Coin
import collections.domain.model.CoinDetails
import collections.domain.model.CoinSet
import collections.domain.model.CoinSetSortOption
import collections.domain.model.CoinSortOption
import collections.domain.model.CollectionStats
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    suspend fun storeCollectionStats(): EmptyNetworkResult<NetworkError>
    fun getCollectionStats(): Flow<CollectionStats?>
    fun getCoins(
        sortBy: CoinSortOption = CoinSortOption.DEFAULT,
        limit: Int = 20,
        setId: String? = null,
    ): Flow<PagingData<Coin>>
    suspend fun storeCoinDetails(id: String): EmptyNetworkResult<NetworkError>
    fun getCoinDetails(id: String): Flow<CoinDetails?>
    suspend fun storeSets(
        sortBy: CoinSetSortOption = CoinSetSortOption.DEFAULT,
    ): EmptyNetworkResult<NetworkError>

    fun getSets(
        sortBy: CoinSetSortOption = CoinSetSortOption.DEFAULT,
    ): Flow<List<CoinSet>>
    suspend fun storeSet(setId: String): EmptyNetworkResult<NetworkError>
    fun getSet(setId: String): Flow<CoinSet?>
    suspend fun createSet(name: String, description: String?): EmptyNetworkResult<NetworkError>
    suspend fun deleteCoins(coinIds: List<String>): NetworkResult<BulkDeleteResponse, NetworkError>
    suspend fun deleteSets(setIds: List<String>): NetworkResult<BulkDeleteResponse, NetworkError>
    suspend fun moveCoinsToSet(
        targetSetId: String,
        coinIds: List<String>
    ): EmptyNetworkResult<NetworkError>
    suspend fun removeCoinsFromSet(
        setId: String,
        coinIds: List<String>
    ): EmptyNetworkResult<NetworkError>

    /**
     * Wipes all user-scoped local collection data. Must be called on session
     * boundaries where the effective user identity changes (logout, login).
     *
     * Account upgrade is intentionally excluded — the userId is preserved
     * and there is no server-side data merge, so clearing would incorrectly
     * show an empty collection for a freshly registered user.
     */
    suspend fun clearUserData()

    suspend fun saveToCollection(
        request: SaveToCollectionRequest
    ): EmptyNetworkResult<NetworkError>
}