package collections.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import collections.data.local.entity.CoinEntity

@Dao
interface CoinDao {

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY createdAt DESC")
    fun pagingSource(setId: String?): PagingSource<Int, CoinEntity>

    @Upsert
    suspend fun upsertAll(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun clearAll()

    @Query("DELETE FROM coins WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("UPDATE coins SET setId = :setId WHERE id IN (:ids)")
    suspend fun setSetIdForIds(ids: List<String>, setId: String?)

    @Transaction
    suspend fun replaceAll(coins: List<CoinEntity>) {
        clearAll()
        if (coins.isNotEmpty()) {
            upsertAll(coins)
        }
    }
}
