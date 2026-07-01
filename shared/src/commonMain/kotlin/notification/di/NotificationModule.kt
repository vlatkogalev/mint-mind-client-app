package notification.di

import app.util.coreComponent
import notification.NotificationTopicManager
import notification.data.NotificationTopicsRepositoryImpl
import notification.domain.NotificationTopicsRepository
import notification.getPushNotificationService
import org.koin.dsl.module

val notificationModule = module {
    single<NotificationTopicsRepository> { NotificationTopicsRepositoryImpl(get()) }

    single {
        NotificationTopicManager(
            notificationTopicStore = coreComponent.notificationTopicStore,
            notificationTopicsRepository = get(),
            pushNotificationService = getPushNotificationService(),
            isAuthenticated = { coreComponent.tokenManager.getToken() != null },
        )
    }
}
