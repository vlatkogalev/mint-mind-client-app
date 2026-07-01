package com.easyauktiongmbh.easyauktion

import androidx.compose.ui.window.ComposeUIViewController
import app.presentation.ui.app.App
import app.util.ApplicationComponent
import app.util.NotificationForegroundSyncObserver
import app.util.NotificationInitializer
import notification.DeepLink
import notification.DeepLinkHandler
import notification.NotificationTopicManager
import org.koin.mp.KoinPlatformTools

fun MainViewController() = ComposeUIViewController { App() }

fun initialize() {
    ApplicationComponent.init()

    val notificationTopicManager =
        KoinPlatformTools.defaultContext().get().get<NotificationTopicManager>()
    NotificationInitializer.onApplicationStart(notificationTopicManager)
    NotificationForegroundSyncObserver.register(notificationTopicManager)
}

/**
 * Bridge function for iOS to handle notification deep links using simple types.
 * Call this from Swift's `userNotificationCenter(_:didReceive:withCompletionHandler:)`.
 */
fun handleNotificationDeepLink(slug: String, auctionId: Long) {
    if (slug.isNotBlank() && auctionId > 0) {
        DeepLinkHandler.offer(DeepLink.Auction(slug = slug, auctionId = auctionId))
    }
}