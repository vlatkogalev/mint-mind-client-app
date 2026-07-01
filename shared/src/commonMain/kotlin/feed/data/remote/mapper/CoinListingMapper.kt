package feed.data.remote.mapper

import feed.data.local.entity.CoinListingEntity
import feed.data.remote.dto.MarketplaceListingsDto
import feed.domain.model.CoinListing

fun MarketplaceListingsDto.toCoinListingEntities(): List<CoinListingEntity> {
    return listings.map {
        CoinListingEntity(
            id = it.id,
            ebayItemId = it.ebayItemId.orEmpty(),
            title = it.title.orEmpty(),
            price = it.price.orEmpty(),
            currency = it.currency.orEmpty(),
            condition = it.condition,
            listingUrl = it.listingUrl.orEmpty(),
            imageUrl = it.imageUrl,
            thumbnailUrl = it.thumbnailUrl,
            buyingOptions = it.buyingOptions
                .mapNotNull { option -> option.trim().takeIf(String::isNotEmpty) }
                .joinToString(separator = ",")
                .ifEmpty { null },
            expiresAt = it.expiresAt,
            timestamp = it.timestamp
        )
    }
}

fun MarketplaceListingsDto.toCoinListing(): List<CoinListing> {
    return listings.map {
        CoinListing(
            id = it.id,
            ebayItemId = it.ebayItemId.orEmpty(),
            title = it.title.orEmpty(),
            price = it.price.orEmpty(),
            currency = it.currency.orEmpty(),
            condition = it.condition,
            listingUrl = it.listingUrl.orEmpty(),
            imageUrl = it.imageUrl,
            thumbnailUrl = it.thumbnailUrl,
            buyingOptions = it.buyingOptions.mapNotNull { option ->
                option.trim().takeIf(String::isNotEmpty)
            },
            expiresAt = it.expiresAt,
            timestamp = it.timestamp
        )
    }
}