package identify.presentation.util

import java.io.File

actual fun readBytesFromPath(path: String): ByteArray? {
    return runCatching { File(path).readBytes() }.getOrNull()
}

actual fun deleteFileAtPath(path: String): Boolean {
    return runCatching { File(path).delete() }.getOrDefault(false)
}
