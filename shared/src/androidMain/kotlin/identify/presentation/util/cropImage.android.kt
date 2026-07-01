package identify.presentation.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import identify.domain.model.CropRect
import io.github.aakira.napier.Napier
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

actual fun cropImage(
    imageData: ByteArray,
    cropRect: CropRect
): ByteArray? {
    return try {
        val originalBitmap = BitmapFactory.decodeByteArray(
            imageData,
            0,
            imageData.size
        ) ?: return null

        val finalX = cropRect.x.coerceIn(0, originalBitmap.width)
        val finalY = cropRect.y.coerceIn(0, originalBitmap.height)
        val finalWidth = cropRect.width.coerceAtMost(originalBitmap.width - finalX)
        val finalHeight = cropRect.height.coerceAtMost(originalBitmap.height - finalY)

        if (finalWidth <= 0 || finalHeight <= 0) {
            Napier.e(tag = "cropImage", message = "Invalid crop dimensions")
            return null
        }

        val croppedBitmap = Bitmap.createBitmap(
            originalBitmap,
            finalX,
            finalY,
            finalWidth,
            finalHeight
        )

        originalBitmap.recycle()

        val exifOrientation = ExifInterface(ByteArrayInputStream(imageData)).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val finalBitmap = croppedBitmap.rotateByExifOrientation(exifOrientation)
        if (finalBitmap !== croppedBitmap) {
            croppedBitmap.recycle()
        }

        val outputStream = ByteArrayOutputStream()
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val result = outputStream.toByteArray()
        finalBitmap.recycle()
        result
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun Bitmap.rotateByExifOrientation(exifOrientation: Int): Bitmap {
    val matrix = Matrix()
    when (exifOrientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.preScale(-1f, 1f)
            matrix.postRotate(270f)
        }

        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.preScale(-1f, 1f)
            matrix.postRotate(90f)
        }

        else -> return this
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
