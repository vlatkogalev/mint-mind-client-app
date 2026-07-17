package collections.presentation.mapper

import collections.domain.model.CoinDetails
import collections.presentation.model.CoinUiModel
import kotlin.math.abs
import kotlin.math.roundToInt

object CoinUiMapper {

    fun map(coin: CoinDetails): CoinUiModel {
        val confidenceLevel = when (coin.aiAnalysis.overallConfidence?.uppercase()) {
            "HIGH" -> CoinUiModel.ConfidenceLevel.HIGH
            "MEDIUM" -> CoinUiModel.ConfidenceLevel.MEDIUM
            else -> CoinUiModel.ConfidenceLevel.LOW
        }

        return CoinUiModel(
            denomination = coin.coinData.denomination
                ?.replaceFirstChar { it.uppercaseChar() } ?: "Unknown",
            subtitle = listOfNotNull(
                coin.coinData.countryOrIssuer?.replaceFirstChar { it.uppercaseChar() },
                coin.coinData.year?.toString(),
                coin.coinData.era,
            ).joinToString(" · "),
            obverseImageUrl = coin.coinData.obverseThumbnailUrl ?: coin.obverseUrl,
            reverseImageUrl = coin.coinData.reverseThumbnailUrl ?: coin.reverseUrl,
            confidenceLabel = "Identified · ${confidenceLevel.displayName()} confidence",
            confidenceLevel = confidenceLevel,
            grade = coin.aiAnalysis.gradeAbbreviation ?: "N/A",
            gradeName = coin.aiAnalysis.gradeName ?: "N/A",
            valueRange = buildValueRange(
                low = coin.aiAnalysis.valueLow,
                high = coin.aiAnalysis.valueHigh,
                currency = coin.aiAnalysis.valueCurrency,
            ),
            valueSubtitle = "Est. retail",
            rarity = coin.aiAnalysis.rarityQualitative ?: "N/A",
            rarityFraction = (coin.aiAnalysis.rarityScore ?: 0.0)
                .toFloat()
                .coerceIn(0.02f, 1f),
            country = coin.coinData.countryOrIssuer
                ?.replaceFirstChar { it.uppercaseChar() } ?: "N/A",
            series = coin.coinData.seriesName ?: "N/A",
            year = coin.coinData.year?.toString() ?: "N/A",
            era = coin.coinData.era ?: "N/A",
            mintMark = buildMintMark(
                mark = coin.coinData.mintMark,
                name = coin.coinData.mintName,
            ),
            obverseDescription = coin.coinData.obverseDescription.orEmpty(),
            obverseLettering = coin.coinData.obverseLettering.orEmpty(),
            obverseDesigner = coin.coinData.designerObverse.orEmpty(),
            reverseDescription = coin.coinData.reverseDescription.orEmpty(),
            reverseLettering = coin.coinData.reverseLettering.orEmpty(),
            reverseDesigner = coin.coinData.designerReverse.orEmpty(),
            specs = buildSpecs(coin),
            catalogueChips = coin.catalogueNumbers.mapNotNull { cn ->
                val name = cn.catalogueName
                val number = cn.number
                if (!name.isNullOrBlank() && !number.isNullOrBlank()) "$name $number"
                else null
            },
            numistaUrl = coin.coinData.numistaUrl.orEmpty(),
            positiveFeatures = coin.aiAnalysis.positiveFeatures.orEmpty(),
            negativeFeatures = coin.aiAnalysis.negativeFeatures.orEmpty(),
            contextBody = buildContextBody(
                analysisNotes = coin.aiAnalysis.analysisNotes,
                historicalContext = coin.coinData.historicalContext,
            ),
            tags = coin.coinData.tags.orEmpty(),
            valueDisclaimer = coin.aiAnalysis.valueDisclaimer.orEmpty(),
        )
    }
}

internal val dummyCoinUi: CoinUiModel = CoinUiMapper.map(CoinDetails.dummyCoin)

private fun buildMintMark(mark: String?, name: String?): String {
    val parts = listOfNotNull(
        mark?.replaceFirstChar { it.uppercaseChar() },
        name,
    ).filter { it.isNotBlank() }
    return parts.joinToString(" · ")
}

private fun buildSpecs(coin: CoinDetails): List<CoinUiModel.SpecEntry> = buildList {
    coin.coinData.weightGrams?.let { add(CoinUiModel.SpecEntry("Weight", "${it.toFixed2()} g")) }
    coin.coinData.diameterMm?.let { add(CoinUiModel.SpecEntry("Diameter", "${it.toFixed2()} mm")) }
    coin.coinData.thicknessMm?.let {
        add(
            CoinUiModel.SpecEntry(
                "Thickness",
                "${it.toFixed2()} mm"
            )
        )
    }
    coin.coinData.edge?.let { add(CoinUiModel.SpecEntry("Edge", it)) }
    coin.coinData.shape?.let { add(CoinUiModel.SpecEntry("Shape", it)) }
    coin.coinData.technique?.let { add(CoinUiModel.SpecEntry("Technique", it)) }
    coin.coinData.mintage?.let { add(CoinUiModel.SpecEntry("Mintage", it.formatWithThousands())) }
    coin.coinData.metalComposition?.let { add(CoinUiModel.SpecEntry("Composition", it)) }
}

private fun buildContextBody(analysisNotes: String?, historicalContext: String?): String {
    if (!analysisNotes.isNullOrBlank()) return analysisNotes
    if (!historicalContext.isNullOrBlank()) {
        return historicalContext.replace(Regex("<[^>]*>"), "").trim()
    }
    return ""
}

private fun CoinUiModel.ConfidenceLevel.displayName(): String = when (this) {
    CoinUiModel.ConfidenceLevel.HIGH -> "High"
    CoinUiModel.ConfidenceLevel.MEDIUM -> "Medium"
    CoinUiModel.ConfidenceLevel.LOW -> "Low"
}

private fun buildValueRange(low: Double?, high: Double?, currency: String?): String {
    val symbol = when (currency?.uppercase()) {
        "EUR" -> "€"
        "USD" -> "$"
        "GBP" -> "£"
        else -> currency ?: ""
    }
    return when {
        low != null && high != null -> "$symbol${low.toCompactCurrency()}–${high.toCompactCurrency()}"
        low != null -> "$symbol${low.toCompactCurrency()}"
        else -> "N/A"
    }
}

private fun Double.toFixed2(): String {
    val rounded = (this * 100).roundToInt()
    val intPart = rounded / 100
    val fracPart = abs(rounded % 100)
    return "$intPart.${fracPart.toString().padStart(2, '0')}"
}

private fun Double.toCompactCurrency(): String = when {
    this >= 1_000_000 -> "${(this / 1_000_000).formatTrimmed()}M"
    this >= 1_000 -> "${(this / 1_000).formatTrimmed()}k"
    else -> toFixed2()
}

private fun Double.formatTrimmed(): String {
    val rounded = (this * 100).roundToInt()
    val intPart = rounded / 100
    val fracPart = abs(rounded % 100)
    return when {
        fracPart == 0 -> "$intPart"
        fracPart % 10 == 0 -> "$intPart.${fracPart / 10}"
        else -> "$intPart.${fracPart.toString().padStart(2, '0')}"
    }
}

private fun Long.formatWithThousands(): String {
    val str = toString()
    val result = StringBuilder()
    str.reversed().forEachIndexed { i, c ->
        if (i > 0 && i % 3 == 0) result.append(',')
        result.append(c)
    }
    return result.reverse().toString()
}
