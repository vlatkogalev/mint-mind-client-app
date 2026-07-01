package identify.presentation.ui.identify

import androidx.compose.ui.graphics.ImageBitmap
import identify.data.remote.dto.CoinAnalysisDto

data class IdentifyResultState(
    val isLoading: Boolean = false,
    val obverseImage: ImageBitmap? = null,
    val reverseImage: ImageBitmap? = null,
    val objectDetails: CoinAnalysisDto? = null,
)

fun IdentifyFlowState.toIdentifyResultState() = IdentifyResultState(
    isLoading = isAddingToCollection,
    obverseImage = obverseImage,
    reverseImage = reverseImage,
    objectDetails = coinAnalysis
)
