package collections.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "coin_data",
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
data class CoinDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val coinId: String,

    val countryOrIssuer: String?,
    val denomination: String?,
    val seriesName: String?,
    val year: Int?,
    val era: String?,
    val mintMark: String?,
    val metalComposition: String?,
    val weightGrams: Double?,
    val diameterMm: Double?,
    val thicknessMm: Double?,
    val edge: String?,
    val obverseDescription: String?,
    val reverseDescription: String?,
    val historicalContext: String?,
    val obverseLettering: String?,
    val reverseLettering: String?,
    val designerObverse: String?,
    val designerReverse: String?,
    val mintage: Long?,
    val shape: String?,
    val technique: String?,
    val orientation: String?,
    val mintName: String?,
    val ruler: String?,
    val objectType: String?,
    val demonetized: Boolean?,
    val tags: List<String>?,
    val numistaUrl: String?,
    val obverseThumbnailUrl: String?,
    val reverseThumbnailUrl: String?,
    val minYear: Int?,
    val maxYear: Int?
)
