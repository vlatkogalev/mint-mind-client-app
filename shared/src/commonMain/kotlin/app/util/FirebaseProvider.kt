package app.util

interface FirebaseProvider {

    /**
     * Logs a custom message that will appear in crash reports.
     */
    fun logMessage(message: String)

    /**
     * Records a non-fatal error/exception. Useful for tracking handled issues or specific events.
     */
    fun recordHandledException(throwable: Throwable)

    /**
     * Sets a custom key-value pair associated with subsequent reports.
     */
    fun setCustomKey(key: String, value: Any)

    /**
     * Logs a custom analytics event.
     *
     * @param name The name of the event. Event names should adhere to Firebase Analytics naming rules (e.g., no spaces, start with letter, etc.).
     *             Reserved event names (starting with '_') should not be used.
     * @param params Optional parameters for the event. Keys should adhere to Firebase Analytics naming rules.
     *               Values should be of types supported by Firebase Analytics parameters (String, Long, Double, Boolean).
     *               Other types will be converted to String. Null values for parameters might be ignored by the SDK.
     */
    fun logEvent(name: String, params: Map<String, Any?>? = null)
}