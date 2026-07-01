package app.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.data.local.AppPreferences
import app.data.local.AppPreferencesImpl
import app.data.local.TokenManager
import app.data.local.TokenManagerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.plus
import notification.data.local.NotificationTopicStore
import notification.data.local.NotificationTopicStoreImpl

interface CoreComponent : CoroutinesComponent {
    val appPreferences: AppPreferences
    val notificationTopicStore: NotificationTopicStore
    val tokenManager: TokenManager
}

internal class CoreComponentImpl internal constructor() : CoreComponent,
    CoroutinesComponent by CoroutinesComponentImpl.create() {

    private val dataStore: DataStore<Preferences> = dataStorePreferences(
        corruptionHandler = null,
        coroutineScope = applicationScope + Dispatchers.IO,
        migrations = emptyList()
    )

    override val appPreferences: AppPreferences = AppPreferencesImpl(dataStore)
    override val notificationTopicStore: NotificationTopicStore =
        NotificationTopicStoreImpl(dataStore)
    override val tokenManager: TokenManager = TokenManagerImpl(dataStore)
}
