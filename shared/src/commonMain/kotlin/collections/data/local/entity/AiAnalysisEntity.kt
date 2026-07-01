package collections.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ai_analysis",
    foreignKeys = [
        ForeignKey(
            entity = CoinDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["coinId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("coinId")]
)
data class AiAnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val coinId: String,

    val overallConfidence: String?,
    val confidenceCountry: String?,
    val confidenceDenomination: String?,
    val confidenceSeries: String?,
    val confidenceYear: String?,
    val confidenceEra: String?,
    val mintMarkStatus: String?,
    val mintMarkConfidence: String?,
    val gradeName: String?,
    val gradeAbbreviation: String?,
    val gradeNumeric: Int?,
    val gradeConfidence: String?,
    val rarityQualitative: String?,
    val rarityScore: Double?,
    val valueLow: Double?,
    val valueHigh: Double?,
    val valueCurrency: String?,
    val positiveFeatures: List<String>?,
    val negativeFeatures: List<String>?,
    val supplySummary: String?,
    val demandSummary: String?,
    val valueDisclaimer: String?,
    val analysisNotes: String?,
    val obverseVisible: Boolean?,
    val reverseVisible: Boolean?,
    val imageFocus: String?,
    val imageLighting: String?,
    val imageResolution: String?,
    val imageCropping: String?,
    val imageIssues: List<String>?,
    val rawJson: String?
)
