package storage.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadUrlsDto(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("uploads")
    val uploads: List<UploadEntry>
) {
    @Serializable
    data class UploadEntry(
        @SerialName("objectKey")
        val objectKey: String,

        @SerialName("uploadUrl")
        val uploadUrl: String
    )
}
