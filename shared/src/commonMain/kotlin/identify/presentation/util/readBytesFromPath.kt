package identify.presentation.util

expect fun readBytesFromPath(path: String): ByteArray?

expect fun deleteFileAtPath(path: String): Boolean
