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
        highlights.mostValuable?.let { coin ->
            CollectionHighlightsEntity(
                type = "mostValuable",
                coinId = coin.id,
                obverseUrl = coin.obverseUrl,
                reverseUrl = coin.reverseUrl,
                obverseThumbnailUrl = coin.obverseThumbnailUrl,
                reverseThumbnailUrl = coin.reverseThumbnailUrl,
                denomination = coin.denomination,
                countryOrIssuer = coin.countryOrIssuer,
                year = coin.year,
                mintage = coin.mintage,
                gradeName = coin.gradeName,
                gradeAbbreviation = coin.gradeAbbreviation,
                gradeNumeric = coin.gradeNumeric,
                estimatedValueMean = coin.estimatedValueMean,
                setId = coin.setId,
                createdAt = coin.createdAt
            )
        },
        highlights.mostAncient?.let { coin ->
            CollectionHighlightsEntity(
                type = "mostAncient",
                coinId = coin.id,
                obverseUrl = coin.obverseUrl,
                reverseUrl = coin.reverseUrl,
                obverseThumbnailUrl = coin.obverseThumbnailUrl,
                reverseThumbnailUrl = coin.reverseThumbnailUrl,
                denomination = coin.denomination,
                countryOrIssuer = coin.countryOrIssuer,
                year = coin.year,
                mintage = coin.mintage,
                gradeName = coin.gradeName,
                gradeAbbreviation = coin.gradeAbbreviation,
                gradeNumeric = coin.gradeNumeric,
                estimatedValueMean = coin.estimatedValueMean,
                setId = coin.setId,
                createdAt = coin.createdAt
            )
        },
        highlights.rarest?.let { coin ->
            CollectionHighlightsEntity(
                type = "rarest",
                coinId = coin.id,
                obverseUrl = coin.obverseUrl,
                reverseUrl = coin.reverseUrl,
                obverseThumbnailUrl = coin.obverseThumbnailUrl,
                reverseThumbnailUrl = coin.reverseThumbnailUrl,
                denomination = coin.denomination,
                countryOrIssuer = coin.countryOrIssuer,
                year = coin.year,
                mintage = coin.mintage,
                gradeName = coin.gradeName,
                gradeAbbreviation = coin.gradeAbbreviation,
                gradeNumeric = coin.gradeNumeric,
                estimatedValueMean = coin.estimatedValueMean,
                setId = coin.setId,
                createdAt = coin.createdAt
            )
        }
    )
}
