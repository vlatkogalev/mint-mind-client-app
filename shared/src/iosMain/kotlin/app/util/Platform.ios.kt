package app.util

import androidx.room.Room
import androidx.room.RoomDatabase
import app.Const.DB_NAME
import app.data.local.AppDatabase
import app.domain.Language
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask
import platform.Foundation.preferredLanguages
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val version: String = UIDevice.currentDevice.systemVersion

    override fun changeLang(lang: String) {
        NSUserDefaults.standardUserDefaults.setObject(arrayListOf(lang), "AppleLanguages")
    }

    override fun getDeviceLanguageTag(): String {
        val tag = NSLocale.preferredLanguages.firstOrNull()
            ?.toString()
            .takeIf { !it.isNullOrEmpty() }
            ?.take(2)
            ?: Language.English.value

        return if (Language.entries.any { it.value == tag }) tag
        else Language.English.value
    }

    override fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFilePath = documentDirectory() + "/$DB_NAME"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}

actual fun getPlatform(): Platform = IOSPlatform()