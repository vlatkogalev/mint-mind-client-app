package collections.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionStatsDto(
    @SerialName("totalCoins")
    val totalCoins: Int,

    @SerialName("totalIssuers")
    val totalIssuers: Int,

    @SerialName("estimatedTotalValueMean")
    val estimatedTotalValueMean: Double,

    @SerialName("highlights")
    val highlights: Highlights?
) {
    @Serializable
    data class Highlights(
        @SerialName("mostValuable")
        val mostValuable: Coin?,

        @SerialName("mostAncient")
        val mostAncient: Coin?,

        @SerialName("rarest")
        val rarest: Coin?
    ) {
        @Serializable
        data class Coin(
            @SerialName("id")
            val id: String,

            @SerialName("obverseUrl")
            val obverseUrl: String,

            @SerialName("reverseUrl")
            val reverseUrl: String,

            @SerialName("denomination")
            val denomination: String,

            @SerialName("countryOrIssuer")
            val countryOrIssuer: String,

            @SerialName("year")
            val year: Int?,

            @SerialName("mintage")
            val mintage: Int,

            @SerialName("gradeName")
            val gradeName: String,

            @SerialName("gradeAbbreviation")
            val gradeAbbreviation: String,

            @SerialName("gradeNumeric")
            val gradeNumeric: Int?,

            @SerialName("estimatedValueMean")
            val estimatedValueMean: Double? = null,

            @SerialName("setId")
            val setId: String?,

            @SerialName("createdAt")
            val createdAt: Long,

            @SerialName("obverseThumbnailUrl")
            val obverseThumbnailUrl: String?,

            @SerialName("reverseThumbnailUrl")
            val reverseThumbnailUrl: String?
        )
    }
}