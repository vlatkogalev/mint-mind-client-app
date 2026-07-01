package app.domain

import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.settings_theme_dark
import mintmind.shared.generated.resources.settings_theme_light
import mintmind.shared.generated.resources.settings_theme_system
import org.jetbrains.compose.resources.StringResource

enum class Theme(val title: StringResource) {
    Light(title = Res.string.settings_theme_light),
    Dark(title = Res.string.settings_theme_dark),
    System(title = Res.string.settings_theme_system)
}