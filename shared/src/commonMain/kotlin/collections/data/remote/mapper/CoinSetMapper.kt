package collections.data.remote.mapper

import collections.data.local.entity.CoinSetEntity
import collections.data.remote.dto.CoinSetDto
import collections.domain.model.CoinSet

fun CoinSetDto.toCoinSetEntity(): CoinSetEntity =
    CoinSetEntity(
        id = id,
        name = name,
        description = description,
        previewObverseUrls = previewObverseUrls,
        coinCount = coinCount,
        createdAt = createdAt
    )

fun List<CoinSetDto>.toCoinSetEntities(): List<CoinSetEntity> =
    map { it.toCoinSetEntity() }

fun CoinSetEntity.toCoinSet(): CoinSet =
    CoinSet(
        id = id,
        name = name,
        description = description,
        previewObverseUrls = previewObverseUrls,
        coinCount = coinCount,
        createdAt = createdAt
    )
