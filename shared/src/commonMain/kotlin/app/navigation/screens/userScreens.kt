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
import app.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel
import user.presentation.ui.user.UserScreen
import user.presentation.ui.user.UserScreenAction
import user.presentation.ui.user.UserViewModel

fun NavGraphBuilder.userScreens(
    navController: NavController
) {
    composable<Screen.User> {
        val viewModel = koinViewModel<UserViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }

        UserScreen(
            state = state,
            snackbarHostState = snackbarHostState
        ) { action ->
            when (action) {
                UserScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                UserScreenAction.CreateUser -> {
                    navController.navigate(Screen.Login)
                }

                else -> {
                    viewModel.onScreenAction(action)
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
}
