package collections.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "catalogue_numbers",
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
data class CatalogueNumberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val coinId: String,

    val catalogueName: String?,

    val number: String?,

    val confidence: String?
)
