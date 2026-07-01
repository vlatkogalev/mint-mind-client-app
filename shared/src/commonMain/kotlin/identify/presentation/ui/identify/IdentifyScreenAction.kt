package identify.presentation.ui.identify

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import com.kashif.cameraK.controller.CameraController
import identify.domain.model.CaptureTarget

sealed interface IdentifyScreenAction {
    data object NavigateUp : IdentifyScreenAction
    data object ShowOffers : IdentifyScreenAction
    data class OnCameraControllerReady(val controller: CameraController) : IdentifyScreenAction
    data class CameraViewParamsChanged(val size: IntSize, val diameter: Int) : IdentifyScreenAction
    data object OnCapture : IdentifyScreenAction
    data class ToggleFlash(val isFlashOn: Boolean) : IdentifyScreenAction
    data class OnImageClick(val image: ImageBitmap) : IdentifyScreenAction
    data object CloseImagePreview : IdentifyScreenAction
    data class RemoveImage(val target: CaptureTarget) : IdentifyScreenAction
    data object CancelIdentification : IdentifyScreenAction
    data object ConfirmIdentification : IdentifyScreenAction
}