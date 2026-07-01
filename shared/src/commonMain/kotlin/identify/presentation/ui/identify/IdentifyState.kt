package identify.presentation.ui.identify

import androidx.compose.ui.graphics.ImageBitmap
import identify.domain.model.CaptureTarget

data class IdentifyState(
    val isLoading: Boolean = false,
    val isIdentifying: Boolean = false,
    val isFlashOn: Boolean = false,
    val obverseImage: ImageBitmap? = null,
    val reverseImage: ImageBitmap? = null,
    val currentCaptureTarget: CaptureTarget? = CaptureTarget.OBVERSE,
    val currentScanningTarget: CaptureTarget? = null,
    val showImagePreview: Boolean = false,
    val previewImage: ImageBitmap? = null,
) {
    val bothImagesCaptured: Boolean
        get() = obverseImage != null && reverseImage != null
}

fun IdentifyFlowState.toIdentifyCaptureState() = IdentifyState(
    isLoading = isLoading,
    isIdentifying = isIdentifying,
    isFlashOn = isFlashOn,
    obverseImage = obverseImage,
    reverseImage = reverseImage,
    currentCaptureTarget = currentCaptureTarget,
    currentScanningTarget = currentScanningTarget,
    showImagePreview = showImagePreview,
    previewImage = previewImage,
)
