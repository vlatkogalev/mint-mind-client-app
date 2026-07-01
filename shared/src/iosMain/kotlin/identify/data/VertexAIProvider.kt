package identify.data

import platform.Foundation.NSData

interface VertexAIProvider {
    suspend fun identifyObject(
        prompt: String,
        obverseSideImageData: NSData,
        reverseSideImageData: NSData
    ): String?
}