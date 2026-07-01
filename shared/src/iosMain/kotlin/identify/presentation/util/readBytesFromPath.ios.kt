package identify.presentation.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.posix.SEEK_END
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.remove
import platform.posix.rewind

@OptIn(ExperimentalForeignApi::class)
actual fun readBytesFromPath(path: String): ByteArray? {
    val file = fopen(path, "rb") ?: return null

    return try {
        if (fseek(file, 0, SEEK_END) != 0) return null

        val fileSize = ftell(file)
        if (fileSize < 0L) return null

        rewind(file)

        val size = fileSize.toInt()
        val bytes = ByteArray(size)
        if (size == 0) return bytes

        val bytesRead = bytes.usePinned {
            fread(it.addressOf(0), 1.convert(), size.convert(), file).toInt()
        }

        if (bytesRead == size) bytes else null
    } finally {
        fclose(file)
    }
}

actual fun deleteFileAtPath(path: String): Boolean {
    return remove(path) == 0
}
