package app.navigation.screens

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.domain.model.ApiResponseEvent
import app.navigation.Screen
import app.util.ApplicationComponent.coreComponent
import auth.presentation.ui.login.LoginScreen
import auth.presentation.ui.login.LoginScreenAction
import auth.presentation.ui.login.LoginViewModel
import auth.presentation.ui.register.RegisterScreen
import auth.presentation.ui.register.RegisterScreenAction
import auth.presentation.ui.register.RegisterViewModel
import auth.presentation.ui.reset_password.ResetPasswordScreen
import auth.presentation.ui.reset_password.ResetPasswordScreenAction
import auth.presentation.ui.reset_password.ResetPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.authScreens(
    navController: NavController
) {
    composable<Screen.Login> {
        val viewModel = koinViewModel<LoginViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }

        LoginScreen(
            state = state,
            snackbarHostState = snackbarHostState,
            onFormEvent = { viewModel.onFormEvent(it) },
        ) { action ->
            when (action) {
                LoginScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                LoginScreenAction.NavigateToRegister -> {
                    navController.navigate(Screen.UserCreate)
                }

                LoginScreenAction.NavigateToResetPassword -> {
                    navController.navigate(Screen.UserResetPassword)
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.apiResponseEvents.collect { event ->
                when (event) {
                    ApiResponseEvent.Success -> {
                        coreComponent.appPreferences.setOnboardingCompleted()
                        navController.navigateUp()
                    }

                    is ApiResponseEvent.Error -> {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.messages.collect { message ->
                snackbarHostState.showSnackbar(
                    message = message.asString(),
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }
        }
    }

    composable<Screen.UserCreate> {
        val viewModel = koinViewModel<RegisterViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }

        RegisterScreen(
            state = state,
            snackbarHostState = snackbarHostState,
            onScreenEvent = { event -> viewModel.onFormEvent(event) }
        ) { action ->
            when (action) {
                RegisterScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                RegisterScreenAction.ToggleConfirmDialog -> {
                    viewModel.toggleConfirmDialog()
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.apiResponseEvents.collect { event ->
                when (event) {
                    ApiResponseEvent.Success -> {
                        viewModel.toggleConfirmDialog()
                    }

                    is ApiResponseEvent.Error -> {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }

    composable<Screen.UserResetPassword> {
        val viewModel = koinViewModel<ResetPasswordViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }

        ResetPasswordScreen(
            state = state,
            snackbarHostState = snackbarHostState,
            onFormEvent = { viewModel.onFormEvent(it) },
        ) { action ->
            when (action) {
                ResetPasswordScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                ResetPasswordScreenAction.ToggleConfirmDialog -> {
                    viewModel.toggleConfirmDialog()
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.apiResponseEvents.collect { event ->
                when (event) {
                    ApiResponseEvent.Success -> {
                        navController.navigateUp()
                    }

                    is ApiResponseEvent.Error -> {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }
}