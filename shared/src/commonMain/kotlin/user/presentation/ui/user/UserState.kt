package user.presentation.ui.user

import app.domain.Language
import app.domain.Theme
import user.domain.model.User

data class UserState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val redirectUri: String? = null,
    val showRedirectDialog: Boolean = false,
    val language: Language = Language.System,
    val showLanguageDialog: Boolean = false,
    val theme: Theme = Theme.System,
    val showThemeDialog: Boolean = false,
    val showLogoutDialog: Boolean = false,
)
