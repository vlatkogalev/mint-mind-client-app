package user.presentation.ui.user

import app.domain.Language
import app.domain.Theme

sealed interface UserScreenAction {
    data object NavigateUp : UserScreenAction
    data object CreateUser : UserScreenAction
    data object ViewUserDetails : UserScreenAction
    data object ChangePassword : UserScreenAction
    data object Logout : UserScreenAction
    data object DismissLogoutDialog : UserScreenAction
    data object ConfirmLogout : UserScreenAction
    data object DeleteUser : UserScreenAction
    data class ChangeLanguage(val language: Language) : UserScreenAction
    data object ToggleLanguageDialog : UserScreenAction
    data class ChangeTheme(val theme: Theme) : UserScreenAction
    data object ToggleThemeDialog : UserScreenAction
    data object RateApp : UserScreenAction
    data object ShareApp : UserScreenAction
    data class ToggleRedirectDialog(val uri: String?) : UserScreenAction
}
