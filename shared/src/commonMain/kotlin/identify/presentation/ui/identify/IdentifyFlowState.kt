package identify.presentation.ui.identify

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import com.kashif.cameraK.controller.CameraController
import identify.data.remote.dto.CoinAnalysisDto
import identify.domain.model.CaptureTarget
import storage.domain.model.UploadSession

@Suppress("ArrayInDataClass")
data class IdentifyFlowState(
    val isLoading: Boolean = false,
    val isIdentifying: Boolean = false,
    val isAddingToCollection: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val hasStoragePermission: Boolean = false,
    val isFlashOn: Boolean = false,
    val isTorchOn: Boolean = false,
    val zoomLevel: Float = 1f,
    val cameraController: CameraController? = null,
    val cameraViewSize: IntSize? = null,
    val spotlightDiameterPx: Int? = null,
    val obverseImage: ImageBitmap? = null,
    val obverseImageData: ByteArray? = null,
    val reverseImage: ImageBitmap? = null,
    val reverseImageData: ByteArray? = null,
    val currentCaptureTarget: CaptureTarget? = CaptureTarget.OBVERSE,
    val currentScanningTarget: CaptureTarget? = null,
    val showImagePreview: Boolean = false,
    val previewImage: ImageBitmap? = null,
    val coinAnalysis: CoinAnalysisDto? = null,
    val uploadSession: UploadSession? = null
)
