package feed.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_listings")
data class CoinListingEntity(
    @PrimaryKey
    val id: String,

    val ebayItemId: String,

    val title: String,

    val price: String,

    val currency: String,

    val condition: String?,

    val listingUrl: String,

    val imageUrl: String?,

    val thumbnailUrl: String?,

    val buyingOptions: String?,

    val expiresAt: Long?,

    val timestamp: Long
)