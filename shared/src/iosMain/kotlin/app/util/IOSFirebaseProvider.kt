package app.util

class IOSFirebaseProvider(
    private val firebaseManager: FirebaseManager,
) : FirebaseProvider {

    override fun logMessage(message: String) {
        firebaseManager.logMessage(message)
    }

    override fun recordHandledException(throwable: Throwable) {
        firebaseManager.recordHandledException(throwable)
    }

    override fun setCustomKey(key: String, value: Any) {
        firebaseManager.setCustomKey(key, value)
    }

    override fun logEvent(name: String, params: Map<String, Any?>?) {
        firebaseManager.logEvent(name = name, params = params)
    }
}