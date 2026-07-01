package app.di

import app.util.FirebaseManager
import app.util.FirebaseProvider
import app.util.IOSFirebaseProvider
import auth.di.authModule
import feed.di.feedModule
import identify.data.AIProvider
import identify.data.IOSAIProvider
import identify.data.VertexAIProvider
import identify.di.identifyModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import notification.di.notificationModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import storage.di.storageModule
import user.di.userModule


fun initializeKoin(
    firebaseManager: FirebaseManager,
    vertexAIProvider: VertexAIProvider,
) {
    Napier.base(DebugAntilog())

    startKoin {
        modules(
            appModule,
            platformModule,
            networkModule,
            notificationModule,
            authModule,
            userModule,
            feedModule,
            identifyModule,
            _root_ide_package_.collections.di.collectionModule,
            storageModule,

            module {
                single<FirebaseProvider> { IOSFirebaseProvider(firebaseManager) }
                single<AIProvider> { IOSAIProvider(get(), vertexAIProvider) }
            }
        )
    }
}

