package collections.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import collections.data.local.entity.CoinSetEntity
import collections.domain.model.CoinSetSortOption
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinSetDao {

    @Query("SELECT * FROM coin_sets ORDER BY createdAt DESC")
    fun getAllSets(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY createdAt ASC")
    fun getAllSetsByDateCreatedOldToNew(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY updatedAt DESC")
    fun getAllSetsByDateUpdatedNewToOld(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY updatedAt ASC")
    fun getAllSetsByDateUpdatedOldToNew(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY coinCount DESC")
    fun getAllSetsByCoinCountHighToLow(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY coinCount ASC")
    fun getAllSetsByCoinCountLowToHigh(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY totalValue DESC")
    fun getAllSetsByTotalValueHighToLow(): Flow<List<CoinSetEntity>>

    @Query("SELECT * FROM coin_sets ORDER BY totalValue ASC")
    fun getAllSetsByTotalValueLowToHigh(): Flow<List<CoinSetEntity>>

    fun getAllSets(sortBy: CoinSetSortOption): Flow<List<CoinSetEntity>> {
        return when (sortBy) {
            CoinSetSortOption.DATE_CREATED_NEW_TO_OLD -> getAllSets()
            CoinSetSortOption.DATE_CREATED_OLD_TO_NEW -> getAllSetsByDateCreatedOldToNew()
            CoinSetSortOption.DATE_UPDATED_NEW_TO_OLD -> getAllSetsByDateUpdatedNewToOld()
            CoinSetSortOption.DATE_UPDATED_OLD_TO_NEW -> getAllSetsByDateUpdatedOldToNew()
            CoinSetSortOption.COIN_COUNT_HIGH_TO_LOW -> getAllSetsByCoinCountHighToLow()
            CoinSetSortOption.COIN_COUNT_LOW_TO_HIGH -> getAllSetsByCoinCountLowToHigh()
            CoinSetSortOption.TOTAL_VALUE_HIGH_TO_LOW -> getAllSetsByTotalValueHighToLow()
            CoinSetSortOption.TOTAL_VALUE_LOW_TO_HIGH -> getAllSetsByTotalValueLowToHigh()
        }
    }

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
