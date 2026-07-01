package feed.domain.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

data class CoinListing(
    val id: String,
    val ebayItemId: String,
    val title: String,
    val price: String,
    val currency: String,
    val condition: String?,
    val imageUrl: String?,
    val listingUrl: String,
    val thumbnailUrl: String?,
    val buyingOptions: List<String>,
    val expiresAt: Long?,
    val timestamp: Long
) {
    companion object {
        val dummyItem = CoinListing(
            id = "1",
            ebayItemId = "12345",
            title = LoremIpsum().values.first().take(90),
            imageUrl = "https://example.com/image.jpg",
            price = "100",
            currency = "USD",
            condition = "New",
            listingUrl = "https://example.com/item",
            thumbnailUrl = "https://example.com/thumbnail.jpg",
            buyingOptions = listOf("BUY_IT_NOW"),
            expiresAt = 1688534400000L,
            timestamp = 1688534400000L,
        )

        fun dummyItemsList(count: Int = 5): List<CoinListing> {
            return (1..count).map {
                dummyItem.copy(id = it.toString(), ebayItemId = "${it}12345")
            }
        }
    }
}
