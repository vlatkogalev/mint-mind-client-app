package app.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
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
            events = viewModel.events,
            snackbarHostState = snackbarHostState
        ) { action ->
            when (action) {
                is CoinScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                else -> viewModel.onScreenAction(action)
            }
        }
    }

    composable<Screen.Set> {
        val args = it.toRoute<Screen.Set>()

        val viewModel = koinViewModel<SetViewModel> { parametersOf(args.id) }
        val state by viewModel.state.collectAsStateWithLifecycle()
        val coins = viewModel.coinsPagingFlow.collectAsLazyPagingItems()
        val snackbarHostState = remember { SnackbarHostState() }

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) viewModel.onScreenResumed()
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        SetScreen(
            state = state,
            coins = coins,
            events = viewModel.events,
            snackbarHostState = snackbarHostState
        ) { action ->
            when (action) {
                is SetScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }
                is SetScreenAction.NavigateToCoin -> {
                    navController.navigate(Screen.Coin(action.coinId))
                }

                else -> viewModel.onScreenAction(action)
            }
        }
    }
}