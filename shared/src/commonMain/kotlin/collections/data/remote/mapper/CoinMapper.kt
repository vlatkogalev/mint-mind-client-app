package collections.data.remote.mapper

import collections.data.remote.dto.CoinsDto
import collections.domain.model.Coin

fun CoinsDto.toCoin(): List<Coin> {
    return coins.map {
        Coin(
            id = it.id,
            obverseUrl = it.obverseUrl,
            reverseUrl = it.reverseUrl,
            obverseThumbnailUrl = it.obverseThumbnailUrl,
            reverseThumbnailUrl = it.reverseThumbnailUrl,
            denomination = it.denomination,
            countryOrIssuer = it.countryOrIssuer,
            year = it.year,
            mintage = it.mintage,
            gradeName = it.gradeName,
            gradeAbbreviation = it.gradeAbbreviation,
            gradeNumeric = it.gradeNumeric,
            estimatedValueMean = it.estimatedValueMean,
            setId = it.setId,
            createdAt = it.createdAt
        )
    }
}