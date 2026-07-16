package collections.data.remote.mapper

import collections.data.local.entity.CoinEntity
import collections.data.remote.dto.SaveToCollectionDto

/**
 * Maps the [POST /coins] response to a local [CoinEntity] so the new coin is visible
 * immediately on the Collection screen without waiting for a [CoinRemoteMediator] refresh.
 *
 * Fields absent from the save response (thumbnail URLs, estimatedValueMean) are set to
 * null. The next mediator refresh overwrites the row via @Upsert on the primary
 * key, filling in enriched values from the list endpoint.
 */
fun SaveToCollectionDto.toCoinEntity(): CoinEntity =
    CoinEntity(
        id = id,
        obverseUrl = obverseUrl,
        reverseUrl = reverseUrl,
        obverseThumbnailUrl = null,
        reverseThumbnailUrl = null,
        denomination = recognitionResult?.denomination ?: "",
        countryOrIssuer = recognitionResult?.countryOrIssuer ?: "",
        year = recognitionResult?.year,
        mintage = recognitionResult?.mintage?.toLong(),
        gradeName = recognitionResult?.gradeName ?: "",
        gradeAbbreviation = recognitionResult?.gradeAbbreviation,
        gradeNumeric = recognitionResult?.gradeNumeric,
        estimatedValueMean = null,
        setId = setId,
        createdAt = createdAt
    )
