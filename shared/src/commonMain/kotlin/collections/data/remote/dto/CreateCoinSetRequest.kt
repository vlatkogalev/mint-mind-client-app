package collections.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCoinSetRequest(
    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null
)
