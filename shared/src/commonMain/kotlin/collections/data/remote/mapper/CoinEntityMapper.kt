package collections.data.remote.mapper

import collections.data.local.entity.CoinEntity
import collections.data.remote.dto.CoinsDto
import collections.domain.model.Coin

fun CoinsDto.toCoinEntities(): List<CoinEntity> {
    return coins.map {
        CoinEntity(
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

fun CoinEntity.toCoin(): Coin {
    return Coin(
        id = id,
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
}
