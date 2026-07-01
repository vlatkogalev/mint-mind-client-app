package feed.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import feed.data.local.entity.CoinListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinListingDao {
    @Query("SELECT * FROM coin_listings ORDER BY timestamp DESC")
    fun getAllCoinListings(): PagingSource<Int, CoinListingEntity>

    @Query("SELECT * FROM coin_listings ORDER BY timestamp DESC LIMIT :limit")
    fun getLatestCoinListings(limit: Int): Flow<List<CoinListingEntity>>

    @Upsert
    suspend fun insert(coinListing: CoinListingEntity)

    @Upsert
    suspend fun insertAll(coinListings: List<CoinListingEntity>)

    @Query("DELETE FROM coin_listings")
    suspend fun deleteAll()
}