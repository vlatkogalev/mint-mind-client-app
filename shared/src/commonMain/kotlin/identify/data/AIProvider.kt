package identify.data

import app.domain.model.Resource
import identify.data.remote.dto.CoinAnalysisDto
import kotlinx.coroutines.flow.Flow

interface AIProvider {
    suspend fun identifyCoin(
        prompt: String,
        obverseSideImageData: ByteArray,
        reverseSideImageData: ByteArray,
    ): Flow<Resource<CoinAnalysisDto>>
}