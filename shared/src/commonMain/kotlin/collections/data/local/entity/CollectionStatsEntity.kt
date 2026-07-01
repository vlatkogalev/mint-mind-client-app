package collections.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_stats")
data class CollectionStatsEntity(
    @PrimaryKey
    val id: Int = 1,

    val totalCoins: Int,

    val totalIssuers: Int,

    val estimatedTotalValueMean: Double
)
