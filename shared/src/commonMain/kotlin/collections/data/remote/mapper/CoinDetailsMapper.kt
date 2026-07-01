package collections.data.remote.mapper

import collections.data.local.entity.AiAnalysisEntity
import collections.data.local.entity.CatalogueNumberEntity
import collections.data.local.entity.CoinDataEntity
import collections.data.local.entity.CoinDetailsEntity
import collections.data.remote.dto.CoinDto
import collections.domain.model.CatalogueNumber
import collections.domain.model.CoinDetails

fun CoinDto.toCoinDetailsEntity(): CoinDetailsEntity = CoinDetailsEntity(
    id = id,
    userId = userId,
    obverseUrl = obverseUrl,
    reverseUrl = reverseUrl,
    setId = setId,
    catalogCoinId = catalogCoinId,
    notes = notes,
    createdAt = createdAt
)

fun CoinDto.toCoinDataEntity(): CoinDataEntity = CoinDataEntity(
    coinId = id,
    countryOrIssuer = coinData.countryOrIssuer,
    denomination = coinData.denomination,
    seriesName = coinData.seriesName,
    year = coinData.year,
    era = coinData.era,
    mintMark = coinData.mintMark,
    metalComposition = coinData.metalComposition,
    weightGrams = coinData.weightGrams,
    diameterMm = coinData.diameterMm,
    thicknessMm = coinData.thicknessMm,
    edge = coinData.edge,
    obverseDescription = coinData.obverseDescription,
    reverseDescription = coinData.reverseDescription,
    historicalContext = coinData.historicalContext,
    obverseLettering = coinData.obverseLettering,
    reverseLettering = coinData.reverseLettering,
    designerObverse = coinData.designerObverse,
    designerReverse = coinData.designerReverse,
    mintage = coinData.mintage,
    shape = coinData.shape,
    technique = coinData.technique,
    orientation = coinData.orientation,
    mintName = coinData.mintName,
    ruler = coinData.ruler,
    objectType = coinData.objectType,
    demonetized = coinData.demonetized,
    tags = coinData.tags?.filterNotNull(),
    numistaUrl = coinData.numistaUrl,
    obverseThumbnailUrl = coinData.obverseThumbnailUrl,
    reverseThumbnailUrl = coinData.reverseThumbnailUrl,
    minYear = coinData.minYear,
    maxYear = coinData.maxYear
)

fun CoinDto.toAiAnalysisEntity(): AiAnalysisEntity = AiAnalysisEntity(
    coinId = id,
    overallConfidence = aiAnalysis.overallConfidence,
    confidenceCountry = aiAnalysis.confidenceCountry,
    confidenceDenomination = aiAnalysis.confidenceDenomination,
    confidenceSeries = aiAnalysis.confidenceSeries,
    confidenceYear = aiAnalysis.confidenceYear,
    confidenceEra = aiAnalysis.confidenceEra,
    mintMarkStatus = aiAnalysis.mintMarkStatus,
    mintMarkConfidence = aiAnalysis.mintMarkConfidence,
    gradeName = aiAnalysis.gradeName,
    gradeAbbreviation = aiAnalysis.gradeAbbreviation,
    gradeNumeric = aiAnalysis.gradeNumeric,
    gradeConfidence = aiAnalysis.gradeConfidence,
    rarityQualitative = aiAnalysis.rarityQualitative,
    rarityScore = aiAnalysis.rarityScore,
    valueLow = aiAnalysis.valueLow,
    valueHigh = aiAnalysis.valueHigh,
    valueCurrency = aiAnalysis.valueCurrency,
    positiveFeatures = aiAnalysis.positiveFeatures?.filterNotNull(),
    negativeFeatures = aiAnalysis.negativeFeatures?.filterNotNull(),
    supplySummary = aiAnalysis.supplySummary,
    demandSummary = aiAnalysis.demandSummary,
    valueDisclaimer = aiAnalysis.valueDisclaimer,
    analysisNotes = aiAnalysis.analysisNotes,
    obverseVisible = aiAnalysis.obverseVisible,
    reverseVisible = aiAnalysis.reverseVisible,
    imageFocus = aiAnalysis.imageFocus,
    imageLighting = aiAnalysis.imageLighting,
    imageResolution = aiAnalysis.imageResolution,
    imageCropping = aiAnalysis.imageCropping,
    imageIssues = aiAnalysis.imageIssues?.filterNotNull(),
    rawJson = aiAnalysis.rawJson
)

fun CoinDto.toCatalogueNumberEntities(): List<CatalogueNumberEntity> =
    catalogueNumbers.map {
        CatalogueNumberEntity(
            coinId = id,
            catalogueName = it.catalogueName,
            number = it.number,
            confidence = it.confidence
        )
    }

fun CoinDto.toCoinDetails(): CoinDetails = CoinDetails(
    id = id,
    userId = userId,
    obverseUrl = obverseUrl,
    reverseUrl = reverseUrl,
    coinData = CoinDetails.CoinData(
        countryOrIssuer = coinData.countryOrIssuer,
        denomination = coinData.denomination,
        seriesName = coinData.seriesName,
        year = coinData.year,
        era = coinData.era,
        mintMark = coinData.mintMark,
        metalComposition = coinData.metalComposition,
        weightGrams = coinData.weightGrams,
        diameterMm = coinData.diameterMm,
        thicknessMm = coinData.thicknessMm,
        edge = coinData.edge,
        obverseDescription = coinData.obverseDescription,
        reverseDescription = coinData.reverseDescription,
        historicalContext = coinData.historicalContext,
        obverseLettering = coinData.obverseLettering,
        reverseLettering = coinData.reverseLettering,
        designerObverse = coinData.designerObverse,
        designerReverse = coinData.designerReverse,
        mintage = coinData.mintage,
        shape = coinData.shape,
        technique = coinData.technique,
        orientation = coinData.orientation,
        mintName = coinData.mintName,
        ruler = coinData.ruler,
        objectType = coinData.objectType,
        demonetized = coinData.demonetized,
        tags = coinData.tags?.filterNotNull(),
        numistaUrl = coinData.numistaUrl,
        obverseThumbnailUrl = coinData.obverseThumbnailUrl,
        reverseThumbnailUrl = coinData.reverseThumbnailUrl,
        minYear = coinData.minYear,
        maxYear = coinData.maxYear
    ),
    aiAnalysis = CoinDetails.AiAnalysis(
        overallConfidence = aiAnalysis.overallConfidence,
        confidenceCountry = aiAnalysis.confidenceCountry,
        confidenceDenomination = aiAnalysis.confidenceDenomination,
        confidenceSeries = aiAnalysis.confidenceSeries,
        confidenceYear = aiAnalysis.confidenceYear,
        confidenceEra = aiAnalysis.confidenceEra,
        mintMarkStatus = aiAnalysis.mintMarkStatus,
        mintMarkConfidence = aiAnalysis.mintMarkConfidence,
        gradeName = aiAnalysis.gradeName,
        gradeAbbreviation = aiAnalysis.gradeAbbreviation,
        gradeNumeric = aiAnalysis.gradeNumeric,
        gradeConfidence = aiAnalysis.gradeConfidence,
        rarityQualitative = aiAnalysis.rarityQualitative,
        rarityScore = aiAnalysis.rarityScore,
        valueLow = aiAnalysis.valueLow,
        valueHigh = aiAnalysis.valueHigh,
        valueCurrency = aiAnalysis.valueCurrency,
        positiveFeatures = aiAnalysis.positiveFeatures?.filterNotNull(),
        negativeFeatures = aiAnalysis.negativeFeatures?.filterNotNull(),
        supplySummary = aiAnalysis.supplySummary,
        demandSummary = aiAnalysis.demandSummary,
        valueDisclaimer = aiAnalysis.valueDisclaimer,
        analysisNotes = aiAnalysis.analysisNotes,
        obverseVisible = aiAnalysis.obverseVisible,
        reverseVisible = aiAnalysis.reverseVisible,
        imageFocus = aiAnalysis.imageFocus,
        imageLighting = aiAnalysis.imageLighting,
        imageResolution = aiAnalysis.imageResolution,
        imageCropping = aiAnalysis.imageCropping,
        imageIssues = aiAnalysis.imageIssues?.filterNotNull(),
        rawJson = aiAnalysis.rawJson
    ),
    catalogueNumbers = catalogueNumbers.map {
        CatalogueNumber(
            catalogueName = it.catalogueName,
            number = it.number,
            confidence = it.confidence
        )
    },
    setId = setId,
    catalogCoinId = catalogCoinId,
    notes = notes,
    createdAt = createdAt
)
