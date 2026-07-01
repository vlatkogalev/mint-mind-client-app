package feed.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketplaceListingsDto(
    @SerialName("listings")
    val listings: List<Listing>,

    @SerialName("nextCursor")
    val nextCursor: Long?
) {
    @Serializable
    data class Listing(
        @SerialName("id")
        val id: String,

        @SerialName("ebayItemId")
        val ebayItemId: String?,

        @SerialName("title")
        val title: String?,

        @SerialName("price")
        val price: String?,

        @SerialName("currency")
        val currency: String?,

        @SerialName("condition")
        val condition: String?,

        @SerialName("listingUrl")
        val listingUrl: String?,

        @SerialName("imageUrl")
        val imageUrl: String?,

        @SerialName("thumbnailUrl")
        val thumbnailUrl: String?,

        @SerialName("buyingOptions")
        val buyingOptions: List<String>,

        @SerialName("expiresAt")
        val expiresAt: Long?,

        @SerialName("timestamp")
        val timestamp: Long
    )
}
