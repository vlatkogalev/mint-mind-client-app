package app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.domain.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppPreferences {
    fun hasCompletedOnboarding(): Flow<Boolean>
    suspend fun setOnboardingCompleted()
    fun getAppLanguage(): Flow<String>
    suspend fun changeAppLanguage(appLang: String): Preferences
    fun getAppTheme(): Flow<String>
    suspend fun changeAppTheme(appTheme: String): Preferences
}

internal class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>,
) : AppPreferences {

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val ONBOARDING_COMPLETED = ".onboardingCompleted"
        private const val APP_LANGUAGE = ".appLanguage"
        private const val APP_THEME = ".appTheme"
    }

    private val onboardingCompletedKey =
        booleanPreferencesKey("$PREFS_TAG_KEY$ONBOARDING_COMPLETED")
    private val appLanguageKey = stringPreferencesKey("$PREFS_TAG_KEY$APP_LANGUAGE")
    private val appThemeKey = stringPreferencesKey("$PREFS_TAG_KEY$APP_THEME")

    override fun hasCompletedOnboarding(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[onboardingCompletedKey] ?: false
    }

    override suspend fun setOnboardingCompleted() {
        dataStore.edit { prefs ->
            prefs[onboardingCompletedKey] = true
        }
    }

    override fun getAppLanguage(): Flow<String> = dataStore.data.map { prefs ->
        prefs[appLanguageKey] ?: ""
    }

    override suspend fun changeAppLanguage(appLang: String): Preferences = dataStore.edit { prefs ->
        prefs[appLanguageKey] = appLang
    }

    override fun getAppTheme(): Flow<String> = dataStore.data.map { prefs ->
        prefs[appThemeKey] ?: Theme.System.name
    }

    override suspend fun changeAppTheme(appTheme: String): Preferences = dataStore.edit { prefs ->
        prefs[appThemeKey] = appTheme
    }
}