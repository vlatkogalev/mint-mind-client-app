package identify.presentation.util

import identify.domain.model.CropRect

/**
 * Crops an image from raw byte data.
 * @param imageData The raw image data (e.g., JPEG or PNG bytes).
 * @param cropRect The rectangle to crop from the original image.
 * @return The cropped image data as bytes, or null if an error occurred.
 */
expect fun cropImage(imageData: ByteArray, cropRect: CropRect): ByteArray?