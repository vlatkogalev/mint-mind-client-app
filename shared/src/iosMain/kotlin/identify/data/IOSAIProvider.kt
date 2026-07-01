package identify.data

import app.domain.model.Resource
import app.presentation.util.UiText
import app.util.retryWithExponentialBackoff
import app.util.toNSData
import identify.data.remote.dto.CoinAnalysisDto
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_identification_failed
import mintmind.shared.generated.resources.identify_coin_identification_error

class IOSAIProvider(
    private val json: Json,
    private val vertexAIProvider: VertexAIProvider,
) : AIProvider {

    private val identificationErrorMessages = listOf(
        "Obverse side Bitmap decoding failed.",
        "Reverse side Bitmap decoding failed.",
        "Object identification failed."
    )

    override suspend fun identifyCoin(
        prompt: String,
        obverseSideImageData: ByteArray,
        reverseSideImageData: ByteArray
    ): Flow<Resource<CoinAnalysisDto>> = flow {
        emit(Resource.loading())

        try {
            val data = retryWithExponentialBackoff(
                retryCondition = { e -> e.message?.contains("Quota exceeded") == true }
            ) {
                vertexAIProvider.identifyObject(
                    prompt = prompt,
                    obverseSideImageData = obverseSideImageData.toNSData(),
                    reverseSideImageData = reverseSideImageData.toNSData()
                )
            }

            data?.let { responseText ->
                try {
                    val cleanJson = responseText
                        .trim()
                        .removePrefix("```json")
                        .removePrefix("```")
                        .removeSuffix("```")

                    Napier.d { cleanJson.replace(Regex("\\s+"), " ") }

                    if (identificationErrorMessages.contains(cleanJson)) {
                        emit(Resource.error(cleanJson, null))
                    }

                    val coinAnalysis = json.decodeFromString<CoinAnalysisDto>(cleanJson)

                    if (coinAnalysis.isCoin) {
                        emit(Resource.success(data = coinAnalysis))
                    } else {
                        val errorMessage = coinAnalysis.analysis?.notes?.takeIf {
                            it.isNotBlank()
                        } ?: "The object is not a coin."

                        emit(Resource.error(errorMessage, null))
                    }
                } catch (e: SerializationException) {
                    Napier.e(
                        tag = "AIProvider",
                        message = "Failed to parse JSON response from AI",
                        throwable = e
                    )
                    Napier.d(tag = "AIProvider", message = "Raw AI response: $responseText")
                    emit(Resource.error("Failed to understand AI response.", null))
                }
            } ?: run {
                emit(
                    Resource.error(
                        UiText.StaticResource(
                            Res.string.identify_coin_identification_error
                        ).asString()
                    )
                )
            }
        } catch (e: Exception) {
            emit(Resource.error(mapToErrorMessage(e.message), null))
        }
    }

    private suspend fun mapToErrorMessage(message: String?): String {
        val uiText = when {
            identificationErrorMessages.any { message?.contains(it, ignoreCase = true) ?: false } ->
                UiText.StaticResource(Res.string.identify_coin_identification_error)

            else -> UiText.StaticResource(Res.string.error_identification_failed, "unknown error")
        }

        return uiText.asString()
    }
}