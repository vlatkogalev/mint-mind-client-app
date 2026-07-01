package identify.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinAnalysisDto(
    @SerialName("type")
    val type: String?,

    @SerialName("isCoin")
    val isCoin: Boolean,

    @SerialName("analysis")
    val analysis: Analysis?,

    @SerialName("catalogReferences")
    val catalogReferences: List<CatalogReference?>?,

    @SerialName("identification")
    val identification: Identification?,

    @SerialName("specifications")
    val specifications: Specifications?,

    @SerialName("rarity")
    val rarity: Rarity?,

    @SerialName("condition")
    val condition: Condition?,

    @SerialName("market")
    val market: Market?,

    @SerialName("design")
    val design: Design?,

    @SerialName("historicalContext")
    val historicalContext: String?,

    @SerialName("imageAnalysis")
    val imageAnalysis: ImageAnalysis?
) {
    @Serializable
    data class Analysis(
        @SerialName("overallConfidence")
        val overallConfidence: String?,

        @SerialName("notes")
        val notes: String?
    )

    @Serializable
    data class CatalogReference(
        @SerialName("source")
        val source: String?,

        @SerialName("id")
        val id: String?,

        @SerialName("confidence")
        val confidence: String?
    )

    @Serializable
    data class Identification(
        @SerialName("country")
        val country: String?,

        @SerialName("denomination")
        val denomination: String?,

        @SerialName("series")
        val series: String?,

        @SerialName("year")
        val year: Int?,

        @SerialName("era")
        val era: String?,

        @SerialName("mintMark")
        val mintMark: MintMark?,

        @SerialName("confidence")
        val confidence: Confidence?
    ) {
        @Serializable
        data class MintMark(
            @SerialName("value")
            val value: String?,

            @SerialName("status")
            val status: String?,

            @SerialName("confidence")
            val confidence: String?
        )

        @Serializable
        data class Confidence(
            @SerialName("country")
            val country: String?,

            @SerialName("denomination")
            val denomination: String?,

            @SerialName("series")
            val series: String?,

            @SerialName("year")
            val year: String?,

            @SerialName("era")
            val era: String?
        )
    }

    @Serializable
    data class Specifications(
        @SerialName("composition")
        val composition: String?,

        @SerialName("weightGrams")
        val weightGrams: Double?,

        @SerialName("diameterMm")
        val diameterMm: Double?,

        @SerialName("thicknessMm")
        val thicknessMm: Double?,

        @SerialName("edge")
        val edge: String?,

        @SerialName("designer")
        val designer: Designer?
    ) {
        @Serializable
        data class Designer(
            @SerialName("obverse")
            val obverse: String?,

            @SerialName("reverse")
            val reverse: String?
        )
    }

    @Serializable
    data class Rarity(
        @SerialName("classification")
        val classification: String?,

        @SerialName("score")
        val score: Double?,

        @SerialName("mintage")
        val mintage: Int?
    )

    @Serializable
    data class Condition(
        @SerialName("grade")
        val grade: Grade?,

        @SerialName("positiveFeatures")
        val positiveFeatures: List<String?>?,

        @SerialName("negativeFeatures")
        val negativeFeatures: List<String?>?
    ) {
        @Serializable
        data class Grade(
            @SerialName("name")
            val name: String?,

            @SerialName("abbreviation")
            val abbreviation: String?,

            @SerialName("numeric")
            val numeric: Int?,

            @SerialName("confidence")
            val confidence: String?
        )
    }

    @Serializable
    data class Market(
        @SerialName("supplySummary")
        val supplySummary: String?,

        @SerialName("demandSummary")
        val demandSummary: String?,

        @SerialName("estimatedValue")
        val estimatedValue: EstimatedValue?
    ) {
        @Serializable
        data class EstimatedValue(
            @SerialName("currency")
            val currency: String?,

            @SerialName("ranges")
            val ranges: List<Range?>?,

            @SerialName("disclaimer")
            val disclaimer: String?
        ) {
            @Serializable
            data class Range(
                @SerialName("label")
                val label: String?,

                @SerialName("low")
                val low: Double?,

                @SerialName("high")
                val high: Double?
            )
        }
    }

    @Serializable
    data class Design(
        @SerialName("obverse")
        val obverse: Obverse?,

        @SerialName("reverse")
        val reverse: Reverse?
    ) {
        @Serializable
        data class Obverse(
            @SerialName("description")
            val description: String?,

            @SerialName("lettering")
            val lettering: String?
        )

        @Serializable
        data class Reverse(
            @SerialName("description")
            val description: String?,

            @SerialName("lettering")
            val lettering: String?
        )
    }

    @Serializable
    data class ImageAnalysis(
        @SerialName("obverseVisible")
        val obverseVisible: Boolean?,

        @SerialName("reverseVisible")
        val reverseVisible: Boolean?,

        @SerialName("imageQuality")
        val imageQuality: ImageQuality?,

        @SerialName("issues")
        val issues: List<String?>?
    ) {
        @Serializable
        data class ImageQuality(
            @SerialName("focus")
            val focus: String?,

            @SerialName("lighting")
            val lighting: String?,

            @SerialName("resolution")
            val resolution: String?,

            @SerialName("cropping")
            val cropping: String?
        )
    }

    companion object {
        val sampleAnalysis = CoinAnalysisDto(
            type = "coin",
            isCoin = true,
            analysis = Analysis(
                overallConfidence = "high",
                notes = "A standard circulation 20 Euro Cent coin from Greece, dated 2023, featuring Ioannis Kapodistrias on the obverse."
            ),
            catalogReferences = listOf(
                CatalogReference(
                    source = "Numista",
                    id = "2182",
                    confidence = "high"
                ),
                CatalogReference(
                    source = "Krause",
                    id = "KM 187",
                    confidence = "medium"
                )
            ),
            identification = Identification(
                country = "Greece",
                denomination = "20 Euro Cent",
                series = "Euro coins",
                year = 2023,
                era = "Eurozone",
                mintMark = Identification.MintMark(
                    value = "Anthemion",
                    status = "visible",
                    confidence = "high"
                ),
                confidence = Identification.Confidence(
                    country = "high",
                    denomination = "high",
                    series = "high",
                    year = "high",
                    era = "high"
                )
            ),
            specifications = Specifications(
                composition = "Nordic Gold (89% Copper, 5% Aluminium, 5% Zinc, 1% Tin)",
                weightGrams = 5.74,
                diameterMm = 22.25,
                thicknessMm = 2.14,
                edge = "Spanish flower shape (smooth with 7 indents)",
                designer = Specifications.Designer(
                    obverse = "Georgios Stamatopoulos",
                    reverse = "Luc Luycx"
                )
            ),
            rarity = Rarity(
                classification = "Very Common",
                score = 0.1,
                mintage = null
            ),
            condition = Condition(
                grade = Condition.Grade(
                    name = "Extremely Fine / About Uncirculated",
                    abbreviation = "XF",
                    numeric = 45,
                    confidence = "medium"
                ),
                positiveFeatures = listOf(
                    "Sharp details in the hair of Ioannis Kapodistrias",
                    "Clear and legible lettering",
                    "Minimal heavy scratches"
                ),
                negativeFeatures = listOf(
                    "Minor circulation marks on both sides",
                    "Slight loss of original mint luster"
                )
            ),
            market = Market(
                supplySummary = "Extremely abundant in circulation across the Eurozone.",
                demandSummary = "Low, primarily face value unless part of an uncirculated set or in perfect MS condition.",
                estimatedValue = Market.EstimatedValue(
                    currency = "EUR",
                    ranges = listOf(
                        Market.EstimatedValue.Range(
                            label = "Circulated",
                            low = 0.2,
                            high = 0.3
                        )
                    ),
                    disclaimer = "This coin is a standard circulation issue and is valued at its face value in average circulated condition."
                )
            ),
            design = Design(
                obverse = Design.Obverse(
                    description = "A portrait of Ioannis Kapodistrias (1776–1831), a prominent Greek politician and diplomat who became the first head of state of independent Greece. To the left is the year of issue and the Athens mintmark (anthemion flower), to the right is the denomination in Greek '20 ΛΕΠΤΑ'.",
                    lettering = "20 ΛΕΠΤΑ 2023 Ι. ΚΑΠΟΔΙΣΤΡΙΑΣ"
                ),
                reverse = Design.Reverse(
                    description = "A map next to the face value, symbolizing the European Union, designed by Luc Luycx.",
                    lettering = "20 EURO CENT LL"
                )
            ),
            historicalContext = "Ioannis Kapodistrias was chosen to represent the 20 cent coin as a leading figure of Greek independence and modernization, serving as the first Governor of the Hellenic State.",
            imageAnalysis = ImageAnalysis(
                obverseVisible = true,
                reverseVisible = true,
                imageQuality = ImageAnalysis.ImageQuality(
                    focus = "high",
                    lighting = "medium",
                    resolution = "high",
                    cropping = "high"
                ),
                issues = emptyList()
            )
        )
    }
}