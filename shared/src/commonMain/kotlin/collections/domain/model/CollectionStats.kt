package collections.domain.model

data class CollectionStats(
    val totalCoins: Int,
    val totalIssuers: Int,
    val estimatedTotalValueMean: Double,
    val highlights: List<HighlightedCoin>?
) {
    data class HighlightedCoin(
        val type: HighlightType,
        val coinId: String,
        val obverseUrl: String,
        val reverseUrl: String,
        val obverseThumbnailUrl: String?,
        val reverseThumbnailUrl: String?,
        val denomination: String,
        val countryOrIssuer: String,
        val year: Int?,
        val mintage: Long?,
        val gradeName: String,
        val gradeAbbreviation: String,
        val gradeNumeric: Int?,
        val estimatedValueMean: Double?,
        val setId: String?,
        val createdAt: Long
    ) {
        val displayObverseUrl: String get() = obverseThumbnailUrl ?: obverseUrl
        val displayReverseUrl: String get() = reverseThumbnailUrl ?: reverseUrl
    }

    enum class HighlightType {
        MOST_VALUABLE,
        MOST_ANCIENT,
        RAREST
    }
}
