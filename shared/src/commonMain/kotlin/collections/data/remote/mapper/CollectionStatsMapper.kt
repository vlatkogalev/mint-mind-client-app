package collections.data.remote.mapper

import collections.data.local.entity.CollectionHighlightsEntity
import collections.data.local.entity.CollectionStatsEntity
import collections.data.remote.dto.CollectionStatsDto

fun CollectionStatsDto.toCollectionStatsEntity(): CollectionStatsEntity = CollectionStatsEntity(
    id = 1,
    totalCoins = totalCoins,
    totalIssuers = totalIssuers,
    estimatedTotalValueMean = estimatedTotalValueMean
)

fun CollectionStatsDto.toCollectionHighlightsEntities(): List<CollectionHighlightsEntity> {
    val highlights = this.highlights ?: return emptyList()
    return listOfNotNull(
        CollectionHighlightsEntity(
            type = "mostValuable",
            coinId = highlights.mostValuable.id,
            obverseUrl = highlights.mostValuable.obverseUrl,
            reverseUrl = highlights.mostValuable.reverseUrl,
            obverseThumbnailUrl = highlights.mostValuable.obverseThumbnailUrl,
            reverseThumbnailUrl = highlights.mostValuable.reverseThumbnailUrl,
            denomination = highlights.mostValuable.denomination,
            countryOrIssuer = highlights.mostValuable.countryOrIssuer,
            year = highlights.mostValuable.year,
            mintage = highlights.mostValuable.mintage,
            gradeName = highlights.mostValuable.gradeName,
            gradeAbbreviation = highlights.mostValuable.gradeAbbreviation,
            gradeNumeric = highlights.mostValuable.gradeNumeric,
            estimatedValueMean = highlights.mostValuable.estimatedValueMean,
            setId = highlights.mostValuable.setId,
            createdAt = highlights.mostValuable.createdAt
        ),
        CollectionHighlightsEntity(
            type = "mostAncient",
            coinId = highlights.mostAncient.id,
            obverseUrl = highlights.mostAncient.obverseUrl,
            reverseUrl = highlights.mostAncient.reverseUrl,
            obverseThumbnailUrl = highlights.mostAncient.obverseThumbnailUrl,
            reverseThumbnailUrl = highlights.mostAncient.reverseThumbnailUrl,
            denomination = highlights.mostAncient.denomination,
            countryOrIssuer = highlights.mostAncient.countryOrIssuer,
            year = highlights.mostAncient.year,
            mintage = highlights.mostAncient.mintage,
            gradeName = highlights.mostAncient.gradeName,
            gradeAbbreviation = highlights.mostAncient.gradeAbbreviation,
            gradeNumeric = highlights.mostAncient.gradeNumeric,
            estimatedValueMean = highlights.mostAncient.estimatedValueMean,
            setId = highlights.mostAncient.setId,
            createdAt = highlights.mostAncient.createdAt
        ),
        CollectionHighlightsEntity(
            type = "rarest",
            coinId = highlights.rarest.id,
            obverseUrl = highlights.rarest.obverseUrl,
            reverseUrl = highlights.rarest.reverseUrl,
            obverseThumbnailUrl = highlights.rarest.obverseThumbnailUrl,
            reverseThumbnailUrl = highlights.rarest.reverseThumbnailUrl,
            denomination = highlights.rarest.denomination,
            countryOrIssuer = highlights.rarest.countryOrIssuer,
            year = highlights.rarest.year,
            mintage = highlights.rarest.mintage,
            gradeName = highlights.rarest.gradeName,
            gradeAbbreviation = highlights.rarest.gradeAbbreviation,
            gradeNumeric = highlights.rarest.gradeNumeric,
            estimatedValueMean = highlights.rarest.estimatedValueMean,
            setId = highlights.rarest.setId,
            createdAt = highlights.rarest.createdAt
        )
    )
}
