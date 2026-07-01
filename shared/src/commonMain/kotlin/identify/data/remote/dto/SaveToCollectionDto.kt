package identify.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveToCollectionDto(
    @SerialName("id")
    val id: String,

    @SerialName("userId")
    val userId: String,

    @SerialName("obverseUrl")
    val obverseUrl: String,

    @SerialName("reverseUrl")
    val reverseUrl: String,

    @SerialName("recognitionResult")
    val recognitionResult: RecognitionResult?,

    @SerialName("catalogueNumbers")
    val catalogueNumbers: List<CatalogueNumber?>?,

    @SerialName("setId")
    val setId: String?,

    @SerialName("catalogCoinId")
    val catalogCoinId: String?,

    @SerialName("notes")
    val notes: String?,

    @SerialName("createdAt")
    val createdAt: Long,

    @SerialName("matchResult")
    val matchResult: MatchResult?
) {
    @Serializable
    data class RecognitionResult(
        @SerialName("overallConfidence")
        val overallConfidence: String?,

        @SerialName("countryOrIssuer")
        val countryOrIssuer: String?,

        @SerialName("denomination")
        val denomination: String?,

        @SerialName("seriesName")
        val seriesName: String?,

        @SerialName("year")
        val year: Int?,

        @SerialName("era")
        val era: String?,

        @SerialName("confidenceCountry")
        val confidenceCountry: String?,

        @SerialName("confidenceDenomination")
        val confidenceDenomination: String?,

        @SerialName("confidenceSeries")
        val confidenceSeries: String?,

        @SerialName("confidenceYear")
        val confidenceYear: String?,

        @SerialName("confidenceEra")
        val confidenceEra: String?,

        @SerialName("mintMark")
        val mintMark: String?,

        @SerialName("mintMarkStatus")
        val mintMarkStatus: String?,

        @SerialName("mintMarkConfidence")
        val mintMarkConfidence: String?,

        @SerialName("metalComposition")
        val metalComposition: String?,

        @SerialName("gradeName")
        val gradeName: String?,

        @SerialName("gradeAbbreviation")
        val gradeAbbreviation: String?,

        @SerialName("gradeNumeric")
        val gradeNumeric: Int?,

        @SerialName("gradeConfidence")
        val gradeConfidence: String?,

        @SerialName("rarityQualitative")
        val rarityQualitative: String?,

        @SerialName("rarityScore")
        val rarityScore: Double?,

        @SerialName("valueLow")
        val valueLow: Double?,

        @SerialName("valueHigh")
        val valueHigh: Double?,

        @SerialName("valueCurrency")
        val valueCurrency: String?,

        @SerialName("mintage")
        val mintage: Int?,

        @SerialName("obverseDescription")
        val obverseDescription: String?,

        @SerialName("reverseDescription")
        val reverseDescription: String?,

        @SerialName("historicalContext")
        val historicalContext: String?,

        @SerialName("obverseVisible")
        val obverseVisible: Boolean?,

        @SerialName("reverseVisible")
        val reverseVisible: Boolean?,

        @SerialName("imageFocus")
        val imageFocus: String?,

        @SerialName("imageLighting")
        val imageLighting: String?,

        @SerialName("imageResolution")
        val imageResolution: String?,

        @SerialName("imageCropping")
        val imageCropping: String?,

        @SerialName("imageIssues")
        val imageIssues: List<String?>?,

        @SerialName("weightGrams")
        val weightGrams: Double?,

        @SerialName("diameterMm")
        val diameterMm: Double?,

        @SerialName("thicknessMm")
        val thicknessMm: Double?,

        @SerialName("edge")
        val edge: String?,

        @SerialName("designerObverse")
        val designerObverse: String?,

        @SerialName("designerReverse")
        val designerReverse: String?,

        @SerialName("positiveFeatures")
        val positiveFeatures: List<String?>?,

        @SerialName("negativeFeatures")
        val negativeFeatures: List<String?>?,

        @SerialName("supplySummary")
        val supplySummary: String?,

        @SerialName("demandSummary")
        val demandSummary: String?,

        @SerialName("valueDisclaimer")
        val valueDisclaimer: String?,

        @SerialName("obverseLettering")
        val obverseLettering: String?,

        @SerialName("reverseLettering")
        val reverseLettering: String?,

        @SerialName("analysisNotes")
        val analysisNotes: String?,

        @SerialName("rawJson")
        val rawJson: String?
    )

    @Serializable
    data class CatalogueNumber(
        @SerialName("catalogueName")
        val catalogueName: String?,

        @SerialName("number")
        val number: String?,

        @SerialName("confidence")
        val confidence: String?
    )

    @Serializable
    data class MatchResult(
        @SerialName("tier")
        val tier: String?,

        @SerialName("bestCandidate")
        val bestCandidate: Candidate?,

        @SerialName("allCandidates")
        val allCandidates: List<Candidate?>?,

        @SerialName("retrievalKey")
        val retrievalKey: String?
    ) {
        @Serializable
        data class Candidate(
            @SerialName("catalogCoinId")
            val catalogCoinId: String?,

            @SerialName("providerName")
            val providerName: String?,

            @SerialName("externalId")
            val externalId: String?,

            @SerialName("score")
            val score: Double?,

            @SerialName("scoreBreakdown")
            val scoreBreakdown: Map<String, Double>?
        )
    }
}
