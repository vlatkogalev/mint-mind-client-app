package app.util

import notification.NotificationTopicManager
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification

object NotificationForegroundSyncObserver {
    private var observer: Any? = null

    fun register(notificationTopicManager: NotificationTopicManager) {
        if (observer != null) return

        observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = null,
        ) { _: NSNotification? -> }
    }
}
