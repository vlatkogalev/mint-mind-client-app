package collections.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import collections.data.local.entity.CollectionHighlightsEntity
import collections.data.local.entity.CollectionStatsEntity
import collections.data.local.entity.CollectionStatsWithHighlights
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionStatsDao {

    @Transaction
    @Query("SELECT * FROM collection_stats WHERE id = 1")
    fun getCollectionStats(): Flow<CollectionStatsWithHighlights?>

    @Upsert
    suspend fun upsertStats(stats: CollectionStatsEntity)

    @Upsert
    suspend fun upsertHighlights(highlights: List<CollectionHighlightsEntity>)

    @Query("DELETE FROM collection_highlights")
    suspend fun deleteAllHighlights()

    @Transaction
    suspend fun upsertCollectionStats(
        stats: CollectionStatsEntity,
        highlights: List<CollectionHighlightsEntity>
    ) {
        upsertStats(stats)
        deleteAllHighlights()
        if (highlights.isNotEmpty()) {
            upsertHighlights(highlights)
        }
    }
}
