package identify.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import app.domain.model.Resource
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import identify.data.remote.dto.CoinAnalysisDto
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class AndroidAIProvider(private val json: Json) : AIProvider {
    private val generativeModel: GenerativeModel by lazy {
        Firebase.ai.generativeModel("gemini-3.5-flash")
    }

    override suspend fun identifyCoin(
        prompt: String,
        obverseSideImageData: ByteArray,
        reverseSideImageData: ByteArray
    ): Flow<Resource<CoinAnalysisDto>> = flow {
        emit(Resource.loading())

        val obverseSideBitmap: Bitmap? = try {
            BitmapFactory.decodeByteArray(obverseSideImageData, 0, obverseSideImageData.size)
        } catch (e: Exception) {
            Napier.e(
                tag = "AIProvider",
                message = "Obverse side Bitmap decoding failed",
                throwable = e
            )
            null
        }

        val reverseSideBitmap: Bitmap? = try {
            BitmapFactory.decodeByteArray(reverseSideImageData, 0, reverseSideImageData.size)
        } catch (e: Exception) {
            Napier.e(
                tag = "AIProvider",
                message = "Reverse side Bitmap decoding failed",
                throwable = e
            )
            null
        }

        if (obverseSideBitmap == null || reverseSideBitmap == null) {
            emit(Resource.error("Failed to decode image.", null))
            return@flow
        }

        try {
            val inputContent = content {
                image(obverseSideBitmap)
                image(reverseSideBitmap)
                text(prompt)
            }

            val response = generativeModel.generateContent(inputContent)

            val responseText = response.text

            if (responseText.isNullOrBlank()) {
                Napier.w(tag = "AIProvider", message = "AI returned a null or empty response.")
                emit(Resource.error("AI returned an empty response.", null))
                return@flow
            }

            try {
                val cleanJson = responseText
                    .trim()
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")

                Napier.d { cleanJson.replace(Regex("\\s+"), " ") }

                val objectAnalysis = json.decodeFromString<CoinAnalysisDto>(cleanJson)

                if (objectAnalysis.isCoin) {
                    emit(Resource.success(data = objectAnalysis))
                } else {
                    val errorMessage = objectAnalysis.analysis?.notes?.takeIf {
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
        } catch (e: Exception) {
            Napier.e(tag = "AIProvider", message = "Object identification failed", throwable = e)
            emit(Resource.error("Object identification failed.", null))
        }
    }
}