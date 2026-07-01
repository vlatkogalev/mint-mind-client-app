package com.vlatkogalev.mintmind

import android.app.Application
import app.di.initializeKoin
import app.util.ApplicationComponent
import app.util.NotificationForegroundSyncObserver
import app.util.NotificationInitializer
import com.google.firebase.FirebaseApp
import notification.NotificationTopicManager
import org.koin.mp.KoinPlatformTools

class MintMind : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeKoin(this)

        ApplicationComponent.init()

        val notificationTopicManager =
            KoinPlatformTools.defaultContext().get().get<NotificationTopicManager>()
        NotificationInitializer.onApplicationStart(notificationTopicManager)
        NotificationForegroundSyncObserver.register(this, notificationTopicManager)

        FirebaseApp.initializeApp(this)
    }
}