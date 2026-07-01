package feed.domain.mapper

import feed.data.local.entity.CoinListingEntity
import feed.domain.model.CoinListing

fun List<CoinListingEntity>.toCoinListingItems(): List<CoinListing> =
    map { it.toCoinListingItem() }

fun CoinListingEntity.toCoinListingItem(): CoinListing = CoinListing(
    id = id,
    ebayItemId = ebayItemId,
    title = title,
    price = price,
    currency = currency,
    condition = condition,
    listingUrl = listingUrl,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl,
    buyingOptions = buyingOptions
        ?.split(',')
        ?.map(String::trim)
        ?.filter(String::isNotEmpty)
        ?: emptyList(),
    expiresAt = expiresAt,
    timestamp = timestamp
)
