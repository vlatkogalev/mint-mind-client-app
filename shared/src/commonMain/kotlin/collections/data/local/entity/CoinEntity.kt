package collections.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "coins",
    indices = [
        Index("setId"),
        Index("createdAt"),
    ]
)
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val obverseUrl: String,
    val reverseUrl: String,
    val obverseThumbnailUrl: String?,
    val reverseThumbnailUrl: String?,
    val denomination: String,
    val countryOrIssuer: String,
    val year: Int?,
    val mintage: Long?,
    val gradeName: String,
    val gradeAbbreviation: String?,
    val gradeNumeric: Int?,
    val estimatedValueMean: Double?,
    val setId: String?,
    val createdAt: Long
)
