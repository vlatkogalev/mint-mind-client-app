package app.presentation.ui.app

import app.domain.Language

data class AppState(
    val isDisconnected: Boolean = false,
    val language: Language = Language.System
)
