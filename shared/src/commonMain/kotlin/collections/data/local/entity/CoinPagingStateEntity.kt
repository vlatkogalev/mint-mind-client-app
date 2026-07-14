package collections.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_paging_state")
data class CoinPagingStateEntity(
    @PrimaryKey
    val queryKey: String,
    val nextCursor: Long?
)
