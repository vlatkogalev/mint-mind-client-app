package app.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import notification.NotificationTopicManager
import notification.domain.model.NotificationTopicSyncReason

object NotificationForegroundSyncObserver : Application.ActivityLifecycleCallbacks {
    private var startedCount = 0
    private lateinit var topicManager: NotificationTopicManager

    fun register(application: Application, notificationTopicManager: NotificationTopicManager) {
        topicManager = notificationTopicManager
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityStarted(activity: Activity) {
        startedCount += 1
        if (startedCount == 1) {
            coreComponent.applicationScope.launch {
                runCatching {
                    topicManager.syncNow(NotificationTopicSyncReason.Foreground)
                }.onFailure { error ->
                    Napier.w(tag = "NotificationTopicSync") {
                        "Foreground sync trigger failed: ${error.message}"
                    }
                }
            }
        }
    }

    override fun onActivityStopped(activity: Activity) {
        startedCount = (startedCount - 1).coerceAtLeast(0)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
