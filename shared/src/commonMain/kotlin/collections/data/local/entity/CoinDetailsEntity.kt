package collections.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_details")
data class CoinDetailsEntity(
    @PrimaryKey
    val id: String,

    val userId: String,

    val obverseUrl: String,

    val reverseUrl: String,

    val setId: String?,

    val catalogCoinId: String?,

    val notes: String?,

    val createdAt: Long
)
