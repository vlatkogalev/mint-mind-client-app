package identify.data.remote.mapper

import identify.data.remote.dto.CoinAnalysisDto
import identify.data.remote.dto.SaveToCollectionRequest
import kotlinx.serialization.json.Json

fun CoinAnalysisDto.toSaveToCollectionRequest(
    obverseKey: String? = null,
    reverseKey: String? = null,
    json: Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }
): SaveToCollectionRequest {
    val estimatedValueRange = market?.estimatedValue?.ranges?.firstOrNull()

    return SaveToCollectionRequest(
        obverseKey = obverseKey,
        reverseKey = reverseKey,
        countryOrIssuer = identification?.country,
        denomination = identification?.denomination,
        seriesName = identification?.series,
        year = identification?.year,
        era = identification?.era,
        mintMark = identification?.mintMark?.value,
        metalComposition = specifications?.composition,
        weightGrams = specifications?.weightGrams,
        diameterMm = specifications?.diameterMm,
        thicknessMm = specifications?.thicknessMm,
        edge = specifications?.edge,
        obverseDescription = design?.obverse?.description,
        reverseDescription = design?.reverse?.description,
        historicalContext = historicalContext,
        obverseLettering = design?.obverse?.lettering,
        reverseLettering = design?.reverse?.lettering,
        designerObverse = specifications?.designer?.obverse,
        designerReverse = specifications?.designer?.reverse,
        mintage = rarity?.mintage,
        overallConfidence = analysis?.overallConfidence,
        confidenceCountry = identification?.confidence?.country,
        confidenceDenomination = identification?.confidence?.denomination,
        confidenceSeries = identification?.confidence?.series,
        confidenceYear = identification?.confidence?.year,
        confidenceEra = identification?.confidence?.era,
        mintMarkStatus = identification?.mintMark?.status,
        mintMarkConfidence = identification?.mintMark?.confidence,
        gradeName = condition?.grade?.name,
        gradeAbbreviation = condition?.grade?.abbreviation,
        gradeNumeric = condition?.grade?.numeric,
        gradeConfidence = condition?.grade?.confidence,
        rarityQualitative = rarity?.classification,
        rarityScore = rarity?.score,
        valueLow = estimatedValueRange?.low,
        valueHigh = estimatedValueRange?.high,
        valueCurrency = market?.estimatedValue?.currency,
        positiveFeatures = condition?.positiveFeatures,
        negativeFeatures = condition?.negativeFeatures,
        supplySummary = market?.supplySummary,
        demandSummary = market?.demandSummary,
        valueDisclaimer = market?.estimatedValue?.disclaimer,
        analysisNotes = analysis?.notes,
        obverseVisible = imageAnalysis?.obverseVisible,
        reverseVisible = imageAnalysis?.reverseVisible,
        imageFocus = imageAnalysis?.imageQuality?.focus,
        imageLighting = imageAnalysis?.imageQuality?.lighting,
        imageResolution = imageAnalysis?.imageQuality?.resolution,
        imageCropping = imageAnalysis?.imageQuality?.cropping,
        imageIssues = imageAnalysis?.issues,
        rawJson = json.encodeToString(this),
        notes = analysis?.notes
    )
}
