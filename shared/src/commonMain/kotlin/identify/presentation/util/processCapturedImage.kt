package identify.presentation.util

import androidx.compose.ui.unit.IntSize
import identify.domain.model.CropRect
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Crops a captured image to match a circular spotlight shown on the camera preview.
 *
 * @param imageData The raw image data from the camera.
 * @param imageWidth The width of the full-resolution image.
 * @param imageHeight The height of the full-resolution image.
 * @param viewSize The width and height of the on-screen CameraPreview in pixels.
 * @param spotlightDiameterPx The diameter of the on-screen spotlight in pixels.
 * @return The cropped image data as bytes, or null if cropping failed.
 */
suspend fun processCapturedImage(
    imageData: ByteArray,
    imageWidth: Int,
    imageHeight: Int,
    viewSize: IntSize,
    spotlightDiameterPx: Int
): ByteArray? {
    if (imageWidth <= 0 || imageHeight <= 0 || viewSize.width <= 0 || viewSize.height <= 0) {
        Napier.e(
            "Invalid dimensions for crop. " +
                    "image=${imageWidth}x$imageHeight, view=${viewSize.width}x${viewSize.height}"
        )
        return null
    }

    val isViewPortrait = viewSize.height >= viewSize.width
    val isImagePortrait = imageHeight >= imageWidth

    // Captured image bytes can be stored in sensor orientation; account for that in scale math.
    val (mappedImageWidth, mappedImageHeight) = if (isViewPortrait != isImagePortrait) {
        imageHeight to imageWidth
    } else {
        imageWidth to imageHeight
    }

    // Camera preview fills its container (center-crop), so mapping uses max scale.
    val scaleX = viewSize.width.toFloat() / mappedImageWidth
    val scaleY = viewSize.height.toFloat() / mappedImageHeight
    val scale = max(scaleX, scaleY)

    val cropSizeImagePx = (spotlightDiameterPx / scale)
        .roundToInt()
        .coerceIn(1, min(imageWidth, imageHeight))

    val cropX = ((imageWidth - cropSizeImagePx) / 2).coerceAtLeast(0)
    val cropY = ((imageHeight - cropSizeImagePx) / 2).coerceAtLeast(0)

    val cropRect = CropRect(x = cropX, y = cropY, width = cropSizeImagePx, height = cropSizeImagePx)

    if (cropRect.width <= 0 || cropRect.height <= 0) {
        Napier.e("Calculated invalid crop rectangle: $cropRect")
        return null
    }

    return withContext(Dispatchers.Default) {
        cropImage(imageData, cropRect)
    }
}
