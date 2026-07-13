package collections.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinSetDto(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("previewObverseUrls")
    val previewObverseUrls: List<String> = emptyList(),

    @SerialName("coinCount")
    val coinCount: Int,

    @SerialName("createdAt")
    val createdAt: Long
)
