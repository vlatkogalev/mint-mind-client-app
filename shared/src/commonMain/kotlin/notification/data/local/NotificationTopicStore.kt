package notification.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

interface NotificationTopicStore {
    fun getSubscribedTopics(): Flow<Set<String>>
    suspend fun getSubscribedTopicsOnce(): Set<String>
    suspend fun setSubscribedTopics(topics: Set<String>)
    suspend fun clearSubscribedTopics()
}

internal class NotificationTopicStoreImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json = Json,
) : NotificationTopicStore {

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val SUBSCRIBED_NOTIFICATION_TOPICS = ".subscribedNotificationTopics"
    }

    private val subscribedTopicsKey =
        stringPreferencesKey("$PREFS_TAG_KEY$SUBSCRIBED_NOTIFICATION_TOPICS")

    override fun getSubscribedTopics(): Flow<Set<String>> = dataStore.data.map { prefs ->
        decodeTopics(prefs[subscribedTopicsKey])
    }

    override suspend fun getSubscribedTopicsOnce(): Set<String> = getSubscribedTopics().first()

    override suspend fun setSubscribedTopics(topics: Set<String>) {
        dataStore.edit { prefs ->
            prefs[subscribedTopicsKey] = encodeTopics(topics)
        }
    }

    override suspend fun clearSubscribedTopics() {
        dataStore.edit { prefs ->
            prefs.remove(subscribedTopicsKey)
        }
    }

    private fun encodeTopics(topics: Set<String>): String =
        json.encodeToString(topics.map { it.trim() }.filter { it.isNotBlank() }.toSet())

    private fun decodeTopics(serialized: String?): Set<String> =
        serialized
            ?.takeIf { it.isNotBlank() }
            ?.let { raw ->
                runCatching {
                    json.decodeFromString<Set<String>>(raw)
                }.getOrElse {
                    decodeLegacyTopics(raw)
                }
            }
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?.toSet()
            ?: emptySet()

    private fun decodeLegacyTopics(serialized: String): Set<String> =
        serialized
            .lineSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toSet()
}
