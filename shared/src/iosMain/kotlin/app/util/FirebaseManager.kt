package app.util

interface FirebaseManager {
    fun logMessage(message: String)
    fun recordHandledException(throwable: Throwable)
    fun setCustomKey(key: String, value: Any)
    fun logEvent(name: String, params: Map<String, Any?>? = null)
}