package storage.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadUrlsRequest(
    @SerialName("fileCount")
    val fileCount: Int
)
