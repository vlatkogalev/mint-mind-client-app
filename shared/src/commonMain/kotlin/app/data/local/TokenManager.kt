package app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TokenManager {
    suspend fun getToken(): String?
    suspend fun getRefreshToken(): String?
    fun hasToken(): Flow<Boolean>
    suspend fun saveTokens(token: String, refreshToken: String): Preferences
    suspend fun deleteTokens(): Preferences
    suspend fun getOrCreateInstallationId(): String
}

internal class TokenManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : TokenManager {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("jwt_refresh_token")
        private val INSTALLATION_ID_KEY = stringPreferencesKey("installation_id")
    }

    private val installationIdMutex = Mutex()
    private var cachedInstallationId: String? = null

    override suspend fun getToken(): String? = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }.first()

    override suspend fun getRefreshToken(): String? = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }.first()

    override fun hasToken(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] != null
    }

    override suspend fun saveTokens(token: String, refreshToken: String): Preferences =
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }

    override suspend fun deleteTokens(): Preferences =
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getOrCreateInstallationId(): String {
        cachedInstallationId?.let { return it }
        installationIdMutex.withLock {
            cachedInstallationId?.let { return it }
            val existing = dataStore.data.map { it[INSTALLATION_ID_KEY] }.first()
            val id = existing ?: Uuid.generateV7().toString().also { newId ->
                dataStore.edit { it[INSTALLATION_ID_KEY] = newId }
            }
            cachedInstallationId = id
            return id
        }
    }
}