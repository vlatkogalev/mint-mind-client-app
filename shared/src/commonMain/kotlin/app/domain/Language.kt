package app.domain

import app.util.getPlatform
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.settings_language_english
import mintmind.shared.generated.resources.settings_language_french
import mintmind.shared.generated.resources.settings_language_german
import mintmind.shared.generated.resources.settings_language_italian
import mintmind.shared.generated.resources.settings_language_system
import org.jetbrains.compose.resources.StringResource

enum class Language(val value: String, val title: StringResource) {
    English(value = "en", title = Res.string.settings_language_english),
    German(value = "de", title = Res.string.settings_language_german),
    French(value = "fr", title = Res.string.settings_language_french),
    Italian(value = "it", title = Res.string.settings_language_italian),
    System(value = "", title = Res.string.settings_language_system);

    val resolvedValue: String
        get() = value.ifEmpty { getPlatform().getDeviceLanguageTag() }

    companion object {
        fun fromValue(value: String): Language =
            entries.firstOrNull { it.value == value } ?: System
    }
}