package identify.presentation.util

import com.kashif.cameraK.utils.toByteArray
import com.kashif.cameraK.utils.toNSData
import identify.domain.model.CropRect
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

@OptIn(ExperimentalForeignApi::class)
actual fun cropImage(
    imageData: ByteArray,
    cropRect: CropRect
): ByteArray? {
    return try {
        val nsData = imageData.toNSData()

        val originalImage = UIImage.imageWithData(nsData) ?: return null
        val originalSize = originalImage.size

        val finalX = cropRect.x.coerceIn(0, originalSize.useContents { width.toInt() })
        val finalY = cropRect.y.coerceIn(0, originalSize.useContents { height.toInt() })
        val finalWidth =
            cropRect.width.coerceAtMost(originalSize.useContents { width.toInt() } - finalX)
        val finalHeight =
            cropRect.height.coerceAtMost(originalSize.useContents { height.toInt() } - finalY)

        if (finalWidth <= 0 || finalHeight <= 0) {
            Napier.e(tag = "cropImage", message = "Invalid crop dimensions")
            return null
        }

        val cropCGRect = CGRectMake(
            x = finalX.toDouble(),
            y = finalY.toDouble(),
            width = finalWidth.toDouble(),
            height = finalHeight.toDouble()
        )

        UIGraphicsBeginImageContextWithOptions(originalSize, false, originalImage.scale)
        originalImage.drawInRect(
            CGRectMake(
                0.0,
                0.0,
                originalSize.useContents { width },
                originalSize.useContents { height })
        )
        val normalizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        if (normalizedImage == null) return null

        val cgImage = normalizedImage.CGImage ?: return null
        val croppedCGImage = CGImageCreateWithImageInRect(cgImage, cropCGRect) ?: return null
        val croppedImage = UIImage.imageWithCGImage(croppedCGImage)
        val jpegData = UIImageJPEGRepresentation(croppedImage, 0.9) ?: return null

        jpegData.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}