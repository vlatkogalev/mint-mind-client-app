package collections.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import collections.data.local.entity.CoinSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinSetDao {

    @Query("SELECT * FROM coin_sets ORDER BY createdAt DESC")
    fun getAllSets(): Flow<List<CoinSetEntity>>

    @Upsert
    suspend fun upsertAll(sets: List<CoinSetEntity>)

    @Query("DELETE FROM coin_sets")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(sets: List<CoinSetEntity>) {
        deleteAll()
        if (sets.isNotEmpty()) {
            upsertAll(sets)
        }
    }
}
