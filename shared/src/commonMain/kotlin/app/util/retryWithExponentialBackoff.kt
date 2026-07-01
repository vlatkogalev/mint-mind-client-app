package app.util

import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay

internal suspend fun <T> retryWithExponentialBackoff(
    maxRetries: Int = 3,
    initialBackoffMs: Long = 5000L,
    retryCondition: (Exception) -> Boolean,
    operation: suspend () -> T,
): T {
    var currentRetry = 0

    while (true) {
        try {
            return operation()
        } catch (e: Exception) {
            if (retryCondition(e) && currentRetry < maxRetries) {
                val delayMs = initialBackoffMs * (1 shl currentRetry)
                Napier.d(
                    message = "Quota exceeded, retrying in ${delayMs}ms (attempt ${currentRetry + 1}/${maxRetries})",
                    tag = "AIProvider"
                )

                delay(delayMs)
                currentRetry++
            } else {
                throw e
            }
        }
    }
}