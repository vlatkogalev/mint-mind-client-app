package app.di

import android.app.Application
import app.util.AndroidFirebaseProvider
import app.util.FirebaseProvider
import auth.di.authModule
import feed.di.feedModule
import identify.data.AIProvider
import identify.data.AndroidAIProvider
import identify.di.identifyModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import notification.di.notificationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import storage.di.storageModule
import user.di.userModule

fun initializeKoin(application: Application) {
    Napier.base(DebugAntilog())

    startKoin {
        androidContext(application)
        modules(
            appModule,
            platformModule,
            networkModule,
            notificationModule,
            authModule,
            userModule,
            feedModule,
            identifyModule,
            collections.di.collectionModule,
            storageModule,

            module {
                single<FirebaseProvider> { AndroidFirebaseProvider() }
                single<AIProvider> { AndroidAIProvider(get()) }
            }
        )
    }
}