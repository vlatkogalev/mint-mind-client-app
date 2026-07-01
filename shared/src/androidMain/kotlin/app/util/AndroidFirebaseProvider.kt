package app.util

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import io.github.aakira.napier.Napier

class AndroidFirebaseProvider : FirebaseProvider {
    private val crashlytics: FirebaseCrashlytics by lazy { Firebase.crashlytics }
    private val analytics: FirebaseAnalytics by lazy { Firebase.analytics }

    override fun logMessage(message: String) {
        crashlytics.log(message)
    }

    override fun recordHandledException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: Any) {
        when (value) {
            is String -> crashlytics.setCustomKey(key, value)
            is Boolean -> crashlytics.setCustomKey(key, value)
            is Double -> crashlytics.setCustomKey(key, value)
            is Float -> crashlytics.setCustomKey(key, value)
            is Int -> crashlytics.setCustomKey(key, value)
            is Long -> crashlytics.setCustomKey(key, value)
            else -> crashlytics.setCustomKey(key, value.toString())
        }
    }

    override fun logEvent(name: String, params: Map<String, Any?>?) {
        Napier.d { "Attempting to log Firebase Analytics event: $name with params: $params" }

        val bundle = params?.let {
            val b = Bundle()
            for ((key, value) in it) {
                when (value) {
                    is String -> b.putString(key, value)
                    is Boolean -> b.putBoolean(key, value)
                    is Double -> b.putDouble(key, value)
                    is Int -> b.putLong(key, value.toLong())
                    is Long -> b.putLong(key, value)
                    is Float -> b.putDouble(key, value.toDouble())
                    null -> {
                        Napier.v { "Ignoring null parameter '$key' for event '$name'" }
                    }

                    else -> {
                        Napier.w { "Unsupported Firebase Analytics parameter type for key '$key': ${value::class.simpleName}. Converting to String for event '$name'." }
                        b.putString(key, value.toString())
                    }
                }
            }
            b
        }
        try {
            analytics.logEvent(name, bundle)
            Napier.d {
                "Called firebaseAnalytics.logEvent(${name}, ${
                    bundle?.keySet()?.joinToString()
                })"
            }
        } catch (e: Exception) {
            Napier.e("Error calling firebaseAnalytics.logEvent for '$name'", e)
        }
    }
}