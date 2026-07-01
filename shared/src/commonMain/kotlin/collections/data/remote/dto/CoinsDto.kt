package collections.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinsDto(
    @SerialName("coins")
    val coins: List<Coin>,

    @SerialName("nextCursor")
    val nextCursor: Long?
) {
    @Serializable
    data class Coin(
        @SerialName("id")
        val id: String,

        @SerialName("obverseUrl")
        val obverseUrl: String,

        @SerialName("reverseUrl")
        val reverseUrl: String,

        @SerialName("obverseThumbnailUrl")
        val obverseThumbnailUrl: String?,

        @SerialName("reverseThumbnailUrl")
        val reverseThumbnailUrl: String?,

        @SerialName("denomination")
        val denomination: String,

        @SerialName("countryOrIssuer")
        val countryOrIssuer: String,

        @SerialName("year")
        val year: Int?,

        @SerialName("mintage")
        val mintage: Long?,

        @SerialName("gradeName")
        val gradeName: String,

        @SerialName("gradeAbbreviation")
        val gradeAbbreviation: String?,

        @SerialName("gradeNumeric")
        val gradeNumeric: Int?,

        @SerialName("estimatedValueMean")
        val estimatedValueMean: Double,

        @SerialName("setId")
        val setId: String?,

        @SerialName("createdAt")
        val createdAt: Long
    )
}
