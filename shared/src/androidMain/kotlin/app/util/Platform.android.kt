package app.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import app.Const.DB_NAME
import app.data.local.AppDatabase
import app.domain.Language

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val version: String = getAppVersionName(applicationContext) ?: "N/A"

    override fun changeLang(lang: String) {
        val localeList = if (lang.isEmpty()) {
            // Restore system default
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(lang)
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    override fun getDeviceLanguageTag(): String {
        val tag = AppCompatDelegate.getApplicationLocales()
            .takeIf { !it.isEmpty }
            ?.toLanguageTags()
            ?.take(2)
            ?: Language.English.value

        return if (Language.entries.any { it.value == tag }) tag
        else Language.English.value
    }

    override fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = applicationContext.getDatabasePath(DB_NAME)

        return Room.databaseBuilder<AppDatabase>(
            context = applicationContext,
            name = dbFile.absolutePath
        )
    }

    private fun getAppVersionName(context: Context): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                ).versionName
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()