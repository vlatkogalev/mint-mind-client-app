package app.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.navigation.Screen
import collections.presentation.ui.coin.CoinScreen
import collections.presentation.ui.coin.CoinScreenAction
import collections.presentation.ui.coin.CoinViewModel
import collections.presentation.ui.set.SetScreen
import collections.presentation.ui.set.SetScreenAction
import collections.presentation.ui.set.SetViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.collectionScreens(
    navController: NavController
) {
    composable<Screen.Coin> {
        val args = it.toRoute<Screen.Coin>()

        val viewModel = koinViewModel<CoinViewModel> { parametersOf(args.id) }
        val state by viewModel.state.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        CoinScreen(
            state = state,
            snackbarHostState = snackbarHostState
        ) { action ->
            when (action) {
                is CoinScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

    composable<Screen.Set> {
        val args = it.toRoute<Screen.Set>()

        val viewModel = koinViewModel<SetViewModel> { parametersOf(args.id) }
        val state by viewModel.state.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        SetScreen(
            state = state,
            snackbarHostState = snackbarHostState
        ) { action ->
            when (action) {
                is SetScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }
}