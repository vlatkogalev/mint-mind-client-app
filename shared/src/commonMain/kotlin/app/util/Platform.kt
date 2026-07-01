package app.util

import androidx.room.RoomDatabase
import app.data.local.AppDatabase
import app.domain.Language

interface Platform {
    val name: String
    val version: String

    fun changeLang(lang: String = Language.English.value)
    fun getDeviceLanguageTag(): String
    fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}

expect fun getPlatform(): Platform