package collections.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import collections.data.local.entity.CoinPagingStateEntity

@Dao
interface CoinPagingStateDao {

    @Upsert
    suspend fun upsert(state: CoinPagingStateEntity)

    @Query("SELECT * FROM coin_paging_state WHERE queryKey = :queryKey")
    suspend fun getCursor(queryKey: String): CoinPagingStateEntity?

    @Query("DELETE FROM coin_paging_state WHERE queryKey = :queryKey")
    suspend fun clear(queryKey: String)

    @Query("DELETE FROM coin_paging_state")
    suspend fun clearAll()
}
