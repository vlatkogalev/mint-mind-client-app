package collections.domain.mapper

import collections.data.local.entity.CollectionHighlightsEntity
import collections.data.local.entity.CollectionStatsWithHighlights
import collections.domain.model.CollectionStats

fun CollectionStatsWithHighlights.toCollectionStats(): CollectionStats = CollectionStats(
    totalCoins = stats.totalCoins,
    totalIssuers = stats.totalIssuers,
    estimatedTotalValueMean = stats.estimatedTotalValueMean,
    highlights = highlights.map { it.toHighlightedCoin() }.ifEmpty { null }
)

private fun CollectionHighlightsEntity.toHighlightedCoin(): CollectionStats.HighlightedCoin =
    CollectionStats.HighlightedCoin(
        type = when (type) {
            "mostValuable" -> CollectionStats.HighlightType.MOST_VALUABLE
            "mostAncient" -> CollectionStats.HighlightType.MOST_ANCIENT
            "rarest" -> CollectionStats.HighlightType.RAREST
            else -> CollectionStats.HighlightType.MOST_VALUABLE
        },
        coinId = coinId,
        obverseUrl = obverseUrl,
        reverseUrl = reverseUrl,
        obverseThumbnailUrl = obverseThumbnailUrl,
        reverseThumbnailUrl = reverseThumbnailUrl,
        denomination = denomination,
        countryOrIssuer = countryOrIssuer,
        year = year,
        mintage = mintage,
        gradeName = gradeName,
        gradeAbbreviation = gradeAbbreviation,
        gradeNumeric = gradeNumeric,
        estimatedValueMean = estimatedValueMean,
        setId = setId,
        createdAt = createdAt
    )
