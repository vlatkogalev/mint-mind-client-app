package collections.presentation.model

data class CoinUiModel(
    val denomination: String,
    val subtitle: String,
    val obverseImageUrl: String,
    val reverseImageUrl: String,
    val confidenceLabel: String,
    val confidenceLevel: ConfidenceLevel,
    val grade: String,
    val gradeName: String,
    val valueRange: String,
    val valueSubtitle: String,
    val rarity: String,
    val rarityFraction: Float,
    val country: String,
    val series: String,
    val year: String,
    val era: String,
    val mintMark: String,
    val obverseDescription: String,
    val obverseLettering: String,
    val obverseDesigner: String,
    val reverseDescription: String,
    val reverseLettering: String,
    val reverseDesigner: String,
    val specs: List<SpecEntry>,
    val catalogueChips: List<String>,
    val numistaUrl: String,
    val positiveFeatures: List<String>,
    val negativeFeatures: List<String>,
    val contextBody: String,
    val tags: List<String>,
    val valueDisclaimer: String,
) {
    data class SpecEntry(val label: String, val value: String)

    enum class ConfidenceLevel { HIGH, MEDIUM, LOW }
}
