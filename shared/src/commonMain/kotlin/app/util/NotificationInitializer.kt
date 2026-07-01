package app.util

import app.domain.Language
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import notification.DeepLink
import notification.DeepLinkHandler
import notification.NotificationTopicManager
import notification.getPushNotificationService

object NotificationInitializer {
    fun onApplicationStart(notificationTopicManager: NotificationTopicManager) {
        getPushNotificationService().onApplicationStartPlatformSpecific()

        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                Napier.i("Push Notification onNewToken: $token")
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                Napier.i("Push Notification notification type message is received: Title: $title and Body: $body")
            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                Napier.i("Push Notification payloadData: $data")
                showLocalizedNotification(data)
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                Napier.i("Notification clicked, Notification payloadData: $data")
                handleNotificationDeepLink(data)
            }
        })
    }

    /**
     * Supported language codes for push notification title resolution.
     * Must match the `title_xx` keys sent by the backend.
     */
    private val SUPPORTED_LANGUAGES = listOf("en", "de", "fr", "it")

    /**
     * Resolves the user's preferred language and displays a local notification
     * using the matching `title_xx` field from the data payload.
     *
     * Falls back to English, then to whatever title is available.
     */
    private fun showLocalizedNotification(data: PayloadData) {
        coreComponent.applicationScope.launch {
            val savedTag = coreComponent.appPreferences.getAppLanguage().first()
            val langCode = Language.fromValue(savedTag).resolvedValue
                .substringBefore("-")   // "de-CH" → "de"
                .lowercase()

            val body = data["title_$langCode"] as? String
                ?: data["title_en"] as? String
                ?: SUPPORTED_LANGUAGES.firstNotNullOfOrNull { data["title_$it"] as? String }

            if (body != null) {
                val payloadStringMap = data.entries
                    .associate { (k, v) -> k to v.toString() }

                NotifierManager.getLocalNotifier().notify(
                    title = "MintMind",
                    body = body,
                    payloadData = payloadStringMap,
                )
            } else {
                Napier.w("Push payload has no localised title to display: $data")
            }
        }
    }

    /**
     * Parses the notification payload for auction deep link data and offers
     * it to [DeepLinkHandler] so the Compose navigation layer can pick it up.
     *
     * Expected payload keys: "slug" and "auctionId".
     */
    fun handleNotificationDeepLink(data: PayloadData) {
        val slug = data["slug"] as? String
        val auctionId = (data["auctionId"] as? String)?.toLongOrNull()
            ?: (data["auctionId"] as? Number)?.toLong()

        if (slug != null && auctionId != null && auctionId > 0) {
            Napier.i("Deep link to auction: slug=$slug, auctionId=$auctionId")
            DeepLinkHandler.offer(DeepLink.Auction(slug = slug, auctionId = auctionId))
        } else {
            Napier.w("Notification payload missing deep link data: slug=$slug, auctionId=$auctionId")
        }
    }
}
