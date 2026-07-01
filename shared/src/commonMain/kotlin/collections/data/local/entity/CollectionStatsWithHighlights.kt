package collections.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionStatsWithHighlights(
    @Embedded
    val stats: CollectionStatsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "statsId"
    )
    val highlights: List<CollectionHighlightsEntity>
)
