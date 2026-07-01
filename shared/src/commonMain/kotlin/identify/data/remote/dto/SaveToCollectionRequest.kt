package identify.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveToCollectionRequest(
    @SerialName("obverseKey")
    val obverseKey: String?,

    @SerialName("reverseKey")
    val reverseKey: String?,

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

    @SerialName("mintMark")
    val mintMark: String?,

    @SerialName("metalComposition")
    val metalComposition: String?,

    @SerialName("weightGrams")
    val weightGrams: Double?,

    @SerialName("diameterMm")
    val diameterMm: Double?,

    @SerialName("thicknessMm")
    val thicknessMm: Double?,

    @SerialName("edge")
    val edge: String?,

    @SerialName("obverseDescription")
    val obverseDescription: String?,

    @SerialName("reverseDescription")
    val reverseDescription: String?,

    @SerialName("historicalContext")
    val historicalContext: String?,

    @SerialName("obverseLettering")
    val obverseLettering: String?,

    @SerialName("reverseLettering")
    val reverseLettering: String?,

    @SerialName("designerObverse")
    val designerObverse: String?,

    @SerialName("designerReverse")
    val designerReverse: String?,

    @SerialName("mintage")
    val mintage: Int?,

    @SerialName("overallConfidence")
    val overallConfidence: String?,

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

    @SerialName("mintMarkStatus")
    val mintMarkStatus: String?,

    @SerialName("mintMarkConfidence")
    val mintMarkConfidence: String?,

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

    @SerialName("analysisNotes")
    val analysisNotes: String?,

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

    @SerialName("rawJson")
    val rawJson: String?,

    @SerialName("notes")
    val notes: String?
)
