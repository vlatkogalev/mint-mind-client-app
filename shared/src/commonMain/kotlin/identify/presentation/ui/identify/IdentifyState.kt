package identify.presentation.ui.identify

import androidx.compose.ui.graphics.ImageBitmap
import identify.domain.model.CaptureTarget

data class IdentifyState(
    val isLoading: Boolean = false,
    val isIdentifying: Boolean = false,
    val isFlashOn: Boolean = false,
    val isTorchOn: Boolean = false,
    val zoomLevel: Float = 1f,
    val obverseImage: ImageBitmap? = null,
    val reverseImage: ImageBitmap? = null,
    val currentCaptureTarget: CaptureTarget? = CaptureTarget.OBVERSE,
    val currentScanningTarget: CaptureTarget? = null,
    val showImagePreview: Boolean = false,
    val previewImage: ImageBitmap? = null,
) {
    val bothImagesCaptured: Boolean
        get() = obverseImage != null && reverseImage != null

    val isBusy: Boolean
        get() = isLoading || isIdentifying

    val fabState: IdentifyFabState
        get() = when {
            isBusy -> IdentifyFabState.Busy
            bothImagesCaptured -> IdentifyFabState.Identify
            else -> IdentifyFabState.Capture
        }

    val zoomLabel: String
        get() = if (zoomLevel % 1f == 0f) "${zoomLevel.toInt()}x" else "${zoomLevel}x"

    fun imageFor(target: CaptureTarget): ImageBitmap? = when (target) {
        CaptureTarget.OBVERSE -> obverseImage
        CaptureTarget.REVERSE -> reverseImage
    }

    fun isCurrentCaptureTarget(target: CaptureTarget): Boolean = currentCaptureTarget == target

    fun isScanningTarget(target: CaptureTarget): Boolean =
        isIdentifying && currentScanningTarget == target
}

enum class IdentifyFabState { Capture, Identify, Busy }

fun IdentifyFlowState.toIdentifyCaptureState() = IdentifyState(
    isLoading = isLoading,
    isIdentifying = isIdentifying,
    isFlashOn = isFlashOn,
    isTorchOn = isTorchOn,
    zoomLevel = zoomLevel,
    obverseImage = obverseImage,
    reverseImage = reverseImage,
    currentCaptureTarget = currentCaptureTarget,
    currentScanningTarget = currentScanningTarget,
    showImagePreview = showImagePreview,
    previewImage = previewImage,
)
