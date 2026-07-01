package identify.presentation.ui.identify

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.Const
import app.domain.model.NetworkResult
import app.domain.model.Resource
import app.domain.toErrorMessage
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.result.ImageCaptureResult
import identify.data.AIProvider
import identify.data.remote.mapper.toSaveToCollectionRequest
import identify.domain.IdentifyRepository
import identify.domain.model.CaptureTarget
import identify.presentation.util.deleteFileAtPath
import identify.presentation.util.processCapturedImage
import identify.presentation.util.readBytesFromPath
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import storage.domain.StorageRepository
import kotlin.time.Duration.Companion.seconds

class IdentifyViewModel(
    private val aiProvider: AIProvider,
    private val identifyRepository: IdentifyRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(IdentifyFlowState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<IdentifyFlowEvent>()
    val events = eventChannel.receiveAsFlow()

    private var scanningAnimationJob: Job? = null

    fun toggleCameraPermission() {
        _state.update { it.copy(hasCameraPermission = !it.hasCameraPermission) }
    }

    fun toggleStoragePermission() {
        _state.update { it.copy(hasStoragePermission = !it.hasStoragePermission) }
    }

    fun toggleFlash(isFlashOn: Boolean) {
        val controller = state.value.cameraController
        if (controller == null) {
            Napier.w("CameraController not ready, cannot toggle flash.")
            return
        }

        try {
            val newFlashMode = if (isFlashOn) FlashMode.ON else FlashMode.OFF
            controller.setFlashMode(newFlashMode)

            _state.update { it.copy(isFlashOn = isFlashOn) }
        } catch (e: Exception) {
            Napier.e("Failed to set flash mode", e)
        }
    }

    fun setCameraController(controller: CameraController) {
        _state.update { it.copy(cameraController = controller) }
    }

    fun onCameraViewParametersChanged(viewSize: IntSize, spotlightDiameter: Int) {
        _state.update {
            it.copy(
                cameraViewSize = viewSize,
                spotlightDiameterPx = spotlightDiameter
            )
        }
    }

    fun clearAllImages() {
        _state.update {
            it.copy(
                obverseImage = null,
                obverseImageData = null,
                reverseImage = null,
                reverseImageData = null,
                currentCaptureTarget = CaptureTarget.OBVERSE,
                currentScanningTarget = null,
                coinAnalysis = null
            )
        }
    }

    fun showImagePreview(image: ImageBitmap) {
        _state.update {
            it.copy(
                showImagePreview = true,
                previewImage = image
            )
        }
    }

    fun closeImagePreview() {
        _state.update {
            it.copy(
                showImagePreview = false,
                previewImage = null
            )
        }
    }

    fun removeImage(target: CaptureTarget) {
        _state.update { currentState ->
            when (target) {
                CaptureTarget.OBVERSE -> {
                    currentState.copy(
                        obverseImage = null,
                        obverseImageData = null,
                        currentCaptureTarget = CaptureTarget.OBVERSE,
                        coinAnalysis = null
                    )
                }

                CaptureTarget.REVERSE -> {
                    currentState.copy(
                        reverseImage = null,
                        reverseImageData = null,
                        currentCaptureTarget = CaptureTarget.REVERSE,
                        coinAnalysis = null
                    )
                }
            }
        }
    }

    fun handleImageCapture() = viewModelScope.launch {
        val cameraController = state.value.cameraController
        if (cameraController == null) {
            Napier.e("Cannot capture image, CameraController is null.")
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        when (val result = cameraController.takePictureToFile()) {
            is ImageCaptureResult.Success -> {
                _state.update { it.copy(isLoading = false) }
                processSuccessfulCapture(result.byteArray)
            }

            is ImageCaptureResult.Error -> {
                _state.update { it.copy(isLoading = false) }
                Napier.e(
                    tag = "IdentifyFlowViewModel",
                    message = "Image Capture Error: ${result.exception.message}"
                )
            }

            is ImageCaptureResult.SuccessWithFile -> {
                Napier.d("Image saved to file: ${result.filePath}")

                val filePath = result.filePath
                val imageData = readBytesFromPath(filePath)
                if (imageData == null) {
                    _state.update { it.copy(isLoading = false) }
                    Napier.e(
                        tag = "IdentifyFlowViewModel",
                        message = "Failed to read captured image from path: $filePath"
                    )
                    val deleted = deleteFileAtPath(filePath)
                    if (!deleted) {
                        Napier.w("Failed to delete unreadable captured image file: $filePath")
                    }
                    return@launch
                }

                processSuccessfulCapture(imageData)

                val deleted = deleteFileAtPath(filePath)
                if (!deleted) {
                    Napier.w("Failed to delete captured image file: $filePath")
                }
            }
        }
    }

    private suspend fun processSuccessfulCapture(imageData: ByteArray) {
        val viewSize = state.value.cameraViewSize
        val spotlightDiameter = state.value.spotlightDiameterPx

        if (viewSize == null || spotlightDiameter == null) {
            Napier.e("Cannot process image: UI parameters (viewSize, spotlightDiameter) are not set.")

            withContext(Dispatchers.Main) {
                _state.update { it.copy(isLoading = false) }
            }

            return
        }

        withContext(Dispatchers.Default) {
            val originalBitmap = imageData.decodeToImageBitmap()

            val croppedImageData = processCapturedImage(
                imageData = imageData,
                imageWidth = originalBitmap.width,
                imageHeight = originalBitmap.height,
                viewSize = viewSize,
                spotlightDiameterPx = spotlightDiameter
            )

            if (croppedImageData == null) {
                Napier.e(
                    tag = "IdentifyFlowViewModel",
                    message = "Image cropping failed or resulted in null data."
                )

                withContext(Dispatchers.Main) {
                    _state.update { it.copy(isLoading = false) }
                }

                return@withContext
            }

            val croppedBitmap = croppedImageData.decodeToImageBitmap()

            withContext(Dispatchers.Main) {
                updateStateWithCapturedImage(croppedImageData, croppedBitmap)
            }
        }
    }

    private fun updateStateWithCapturedImage(data: ByteArray, bitmap: ImageBitmap) {
        _state.update { currentState ->
            val target = currentState.currentCaptureTarget ?: return@update currentState.copy(
                isLoading = false
            )

            val updatedState = when (target) {
                CaptureTarget.OBVERSE -> {
                    val nextTarget =
                        if (currentState.reverseImage == null) CaptureTarget.REVERSE else null

                    currentState.copy(
                        obverseImage = bitmap,
                        obverseImageData = data,
                        currentCaptureTarget = nextTarget,
                        coinAnalysis = null
                    )
                }

                CaptureTarget.REVERSE -> {
                    val nextTarget =
                        if (currentState.obverseImage == null) CaptureTarget.OBVERSE else null

                    currentState.copy(
                        reverseImage = bitmap,
                        reverseImageData = data,
                        currentCaptureTarget = nextTarget,
                        coinAnalysis = null
                    )
                }
            }

            updatedState.copy(isLoading = false)
        }
    }

    fun identifyCoin() = viewModelScope.launch {
        val obverse = state.value.obverseImageData
        val reverse = state.value.reverseImageData

        if (obverse == null || reverse == null) {
            Napier.w("identifyCoin called without both images being present.")
            return@launch
        }

        startScanningAnimation()

        aiProvider.identifyCoin(
            prompt = Const.IDENTIFY_PROMPT,
            obverseSideImageData = obverse,
            reverseSideImageData = reverse
        ).collect { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    _state.update { it.copy(isIdentifying = true) }
                }

                Resource.Status.SUCCESS -> {
                    val analysisDto = result.data

                    if (analysisDto == null) {
                        stopScanningAnimation()
                        eventChannel.send(IdentifyFlowEvent.ShowMessage("Received success status with no data."))
                        return@collect
                    }

                    stopScanningAnimation()
                    _state.update { it.copy(coinAnalysis = analysisDto) }

                    fetchUploadUrls()

                    eventChannel.send(IdentifyFlowEvent.NavigateToResult)
                }

                Resource.Status.ERROR -> {
                    stopScanningAnimation()
                    eventChannel.send(
                        IdentifyFlowEvent.ShowMessage(
                            result.message ?: "Error"
                        )
                    )
                }
            }
        }
    }

    private fun fetchUploadUrls() = viewModelScope.launch {
        when (val result = storageRepository.getUploadUrls(fileCount = 2)) {
            is NetworkResult.Success -> {
                _state.update { it.copy(uploadSession = result.data) }
            }

            is NetworkResult.Error -> {
                Napier.w("Failed to fetch upload URLs: ${result.error}")
            }
        }
    }

    private fun startScanningAnimation() {
        scanningAnimationJob?.cancel()
        _state.update {
            it.copy(
                isIdentifying = true,
                currentScanningTarget = CaptureTarget.OBVERSE
            )
        }
        scanningAnimationJob = viewModelScope.launch {
            while (state.value.isIdentifying) {
                _state.update { it.copy(currentScanningTarget = CaptureTarget.OBVERSE) }
                delay(2.seconds)

                if (state.value.isIdentifying) {
                    _state.update { it.copy(currentScanningTarget = CaptureTarget.REVERSE) }
                    delay(2.seconds)
                }
            }
        }
    }

    private fun stopScanningAnimation() {
        scanningAnimationJob?.cancel()
        scanningAnimationJob = null
        _state.update {
            it.copy(
                isIdentifying = false,
                currentScanningTarget = null
            )
        }
    }

    fun addToCollection() = viewModelScope.launch {
        val analysis = state.value.coinAnalysis
        val obverseData = state.value.obverseImageData
        val reverseData = state.value.reverseImageData

        if (analysis == null) {
            eventChannel.send(IdentifyFlowEvent.ShowMessage("No identified coin to add."))
            return@launch
        }

        if (obverseData == null || reverseData == null) {
            eventChannel.send(IdentifyFlowEvent.ShowMessage("Missing captured images."))
            return@launch
        }

        _state.update { it.copy(isAddingToCollection = true) }

        var uploadSession = state.value.uploadSession
        if (uploadSession == null) {
            when (val result = storageRepository.getUploadUrls(fileCount = 2)) {
                is NetworkResult.Success -> {
                    uploadSession = result.data
                    _state.update { it.copy(uploadSession = uploadSession) }
                }

                is NetworkResult.Error -> {
                    _state.update { it.copy(isAddingToCollection = false) }
                    eventChannel.send(
                        IdentifyFlowEvent.ShowMessage(result.error.toErrorMessage().asString())
                    )
                    return@launch
                }
            }
        }

        val obverseUpload = uploadSession.uploads.getOrNull(0)
        val reverseUpload = uploadSession.uploads.getOrNull(1)

        if (obverseUpload == null || reverseUpload == null) {
            _state.update { it.copy(isAddingToCollection = false) }
            eventChannel.send(IdentifyFlowEvent.ShowMessage("No upload targets available."))
            return@launch
        }

        val (obverseUploadResult, reverseUploadResult) = coroutineScope {
            val obverseJob =
                async { storageRepository.uploadFile(obverseUpload.uploadUrl, obverseData) }
            val reverseJob =
                async { storageRepository.uploadFile(reverseUpload.uploadUrl, reverseData) }
            obverseJob.await() to reverseJob.await()
        }

        if (obverseUploadResult is NetworkResult.Error || reverseUploadResult is NetworkResult.Error) {
            _state.update { it.copy(isAddingToCollection = false) }
            Napier.e("Image upload failed. Obverse: $obverseUploadResult, Reverse: $reverseUploadResult")
            eventChannel.send(IdentifyFlowEvent.ShowMessage("Failed to upload coin images. Please try again."))
            return@launch
        }

        val request = analysis.toSaveToCollectionRequest(
            obverseKey = obverseUpload.objectKey,
            reverseKey = reverseUpload.objectKey
        )

        when (val saveResult = identifyRepository.saveToCollection(request)) {
            is NetworkResult.Success -> {
                _state.update { it.copy(isAddingToCollection = false) }
                resetSession()
                eventChannel.send(IdentifyFlowEvent.ShowMessage("Added to collection."))
                eventChannel.send(IdentifyFlowEvent.NavigateToIdentifyFlow)
            }

            is NetworkResult.Error -> {
                _state.update { it.copy(isAddingToCollection = false) }
                eventChannel.send(
                    IdentifyFlowEvent.ShowMessage(saveResult.error.toErrorMessage().asString())
                )
            }
        }
    }

    fun scanAgain() = viewModelScope.launch {
        resetSession()
        eventChannel.send(IdentifyFlowEvent.NavigateToIdentifyFlow)
    }

    private fun resetSession() {
        stopScanningAnimation()
        _state.update {
            IdentifyFlowState(
                hasCameraPermission = it.hasCameraPermission,
                hasStoragePermission = it.hasStoragePermission,
                cameraController = it.cameraController,
                cameraViewSize = it.cameraViewSize,
                spotlightDiameterPx = it.spotlightDiameterPx
            )
        }
    }
}
