package user.presentation.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.domain.Language
import app.domain.Theme
import app.domain.toErrorMessage
import app.presentation.util.UiText
import app.util.coreComponent
import app.util.getPlatform
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import user.domain.UserRepository
import user.domain.usecase.LogoutResult
import user.domain.usecase.LogoutUseCase

class UserViewModel(
    private val userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val platform = getPlatform()
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    private val messageChannel = Channel<UiText> { }
    val messages = messageChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.getUser(),
                coreComponent.appPreferences.getAppTheme(),
                coreComponent.appPreferences.getAppLanguage()
            ) { user, theme, language ->
                Triple(user, theme, language)
            }.collect { (user, theme, language) ->
                _state.update { state ->
                    state.copy(
                        user = user,
                        theme = when (theme) {
                            Theme.Light.name -> Theme.Light
                            Theme.Dark.name -> Theme.Dark
                            Theme.System.name -> Theme.System
                            else -> state.theme
                        },
                        language = Language.fromValue(language)
                    )
                }
            }
        }
    }

    fun onScreenAction(action: UserScreenAction) {
        when (action) {
            UserScreenAction.ToggleLanguageDialog -> toggleLanguageDialog()
            UserScreenAction.ToggleThemeDialog -> toggleThemeDialog()
            is UserScreenAction.ChangeLanguage -> saveAppLanguage(action.language)
            is UserScreenAction.ChangeTheme -> saveAppTheme(action.theme)
            is UserScreenAction.ToggleRedirectDialog -> toggleRedirectDialog(action.uri)
            UserScreenAction.Logout -> showLogoutDialog()
            UserScreenAction.DismissLogoutDialog -> dismissLogoutDialog()
            UserScreenAction.ConfirmLogout -> logout()
            else -> Unit
        }
    }

    private fun showLogoutDialog() {
        if (state.value.isLoading) return
        _state.update { it.copy(showLogoutDialog = true) }
    }

    private fun dismissLogoutDialog() {
        _state.update { it.copy(showLogoutDialog = false) }
    }

    private fun logout() {
        if (state.value.isLoading) return

        _state.update { it.copy(isLoading = true, showLogoutDialog = false) }

        viewModelScope.launch {
            when (val result = logoutUseCase()) {
                LogoutResult.Success -> Unit
                is LogoutResult.LogoutFailed -> messageChannel.send(result.error.toErrorMessage())
                is LogoutResult.AnonymousAuthFailed -> messageChannel.send(result.error.toErrorMessage())
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun toggleRedirectDialog(uri: String? = null) {
        _state.update {
            it.copy(
                redirectUri = uri,
                showRedirectDialog = !state.value.showRedirectDialog
            )
        }
    }

    private fun toggleThemeDialog() {
        _state.update { it.copy(showThemeDialog = !state.value.showThemeDialog) }
    }

    private fun saveAppTheme(appTheme: Theme) = viewModelScope.launch {
        coreComponent.appPreferences.changeAppTheme(appTheme = appTheme.name)
    }

    private fun toggleLanguageDialog() {
        _state.update { it.copy(showLanguageDialog = !state.value.showLanguageDialog) }
    }

    private fun saveAppLanguage(appLang: Language) = viewModelScope.launch {
        coreComponent.appPreferences.changeAppLanguage(appLang.value).also {
            platform.changeLang(appLang.value)
        }
    }
}
