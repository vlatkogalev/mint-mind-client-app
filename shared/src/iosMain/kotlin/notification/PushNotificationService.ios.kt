package notification

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

class IOSPushNotificationService : PushNotificationService {
    override fun onApplicationStartPlatformSpecific() {
        NotifierManager.initialize(
            NotificationPlatformConfiguration.Ios(
                showPushNotification = true,
                askNotificationPermissionOnStart = true
            )
        )
    }

    override suspend fun subscribeToTopic(topic: String) {
        NotifierManager.getPushNotifier().subscribeToTopic(topic)
    }

    override suspend fun unsubscribeFromTopic(topic: String) {
        NotifierManager.getPushNotifier().unSubscribeFromTopic(topic)
    }
}

actual fun getPushNotificationService(): PushNotificationService = IOSPushNotificationService()
