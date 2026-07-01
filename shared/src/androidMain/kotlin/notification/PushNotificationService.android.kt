package notification

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.vlatkogalev.mintmind.shared.R

class AndroidPushNotificationService : PushNotificationService {
    override fun onApplicationStartPlatformSpecific() {
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = true
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

actual fun getPushNotificationService(): PushNotificationService = AndroidPushNotificationService()
