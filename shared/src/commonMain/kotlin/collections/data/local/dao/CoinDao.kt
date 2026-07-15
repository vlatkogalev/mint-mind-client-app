package collections.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import collections.data.local.entity.CoinEntity
import collections.domain.model.CoinSortOption

@Dao
interface CoinDao {

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY createdAt DESC")
    fun pagingSource(setId: String?): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY estimatedValueMean IS NULL, estimatedValueMean DESC")
    fun pagingSourceByValueHighToLow(setId: String?): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY estimatedValueMean IS NULL, estimatedValueMean ASC")
    fun pagingSourceByValueLowToHigh(setId: String?): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY year IS NULL, year DESC")
    fun pagingSourceByReleaseYearNewToOld(setId: String?): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY year IS NULL, year ASC")
    fun pagingSourceByReleaseYearOldToNew(setId: String?): PagingSource<Int, CoinEntity>

    fun pagingSourceWithSort(
        setId: String?,
        sortBy: CoinSortOption
    ): PagingSource<Int, CoinEntity> {
        return when (sortBy) {
            CoinSortOption.DATE_ADDED_NEW_TO_OLD -> pagingSource(setId)
            CoinSortOption.DATE_ADDED_OLD_TO_NEW -> pagingSourceByDateAddedOldToNew(setId)
            CoinSortOption.VALUE_HIGH_TO_LOW -> pagingSourceByValueHighToLow(setId)
            CoinSortOption.VALUE_LOW_TO_HIGH -> pagingSourceByValueLowToHigh(setId)
            CoinSortOption.RELEASE_YEAR_NEW_TO_OLD -> pagingSourceByReleaseYearNewToOld(setId)
            CoinSortOption.RELEASE_YEAR_OLD_TO_NEW -> pagingSourceByReleaseYearOldToNew(setId)
        }
    }

    @Query("SELECT * FROM coins WHERE (:setId IS NULL OR setId = :setId) ORDER BY createdAt ASC")
    fun pagingSourceByDateAddedOldToNew(setId: String?): PagingSource<Int, CoinEntity>

    @Upsert
    suspend fun upsertAll(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun clearAll()

    @Query("DELETE FROM coins WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("UPDATE coins SET setId = :setId WHERE id IN (:ids)")
    suspend fun setSetIdForIds(ids: List<String>, setId: String?)

    @Query("UPDATE coins SET setId = NULL WHERE setId = :setId AND id NOT IN (:ids)")
    suspend fun clearSetIdForCoinsNotIn(setId: String, ids: List<String>)

    @Transaction
    suspend fun replaceAll(coins: List<CoinEntity>) {
        clearAll()
        if (coins.isNotEmpty()) {
            upsertAll(coins)
        }
    }
}
