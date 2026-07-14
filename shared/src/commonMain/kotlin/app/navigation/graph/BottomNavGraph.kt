package app.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import app.navigation.Screen
import app.navigation.transition.BottomNavTransitions
import app.presentation.ui.home.HomeScreen
import app.presentation.ui.home.HomeScreenAction
import app.presentation.ui.home.HomeViewModel
import collections.presentation.ui.collection.CollectionScreen
import collections.presentation.ui.collection.CollectionScreenAction
import collections.presentation.ui.collection.CollectionViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
        enterTransition = BottomNavTransitions.enterTransition,
        exitTransition = BottomNavTransitions.exitTransition,
        popEnterTransition = BottomNavTransitions.popEnterTransition,
        popExitTransition = BottomNavTransitions.popExitTransition
    ) {
        composable<Screen.Home> {
            val viewModel = koinViewModel<HomeViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            val snackbarHostState = remember { SnackbarHostState() }

            HomeScreen(
                state = state,
                snackbarHostState = snackbarHostState
            ) { action ->
                when (action) {
                    HomeScreenAction.NavigateToUser -> {
                        parentNavController.navigate(Screen.User)
                    }

                    HomeScreenAction.NavigateToIdentify -> {
                        parentNavController.navigate(Screen.IdentifyGraph)
                    }

                    HomeScreenAction.NavigateToNewsFeed -> {
                        parentNavController.navigate(Screen.NewsFeed)
                    }

                    HomeScreenAction.NavigateToCoinListings -> {
                        parentNavController.navigate(Screen.CoinListings)
                    }

                    is HomeScreenAction.ToggleRedirectDialog -> {
                        viewModel.toggleRedirectDialog(action.redirectUri)
                    }
                }
            }
        }

        composable<Screen.Collection> {
            val viewModel = koinViewModel<CollectionViewModel>()
            val state by viewModel.state.collectAsState()
            val coins = viewModel.coinsPagingFlow.collectAsLazyPagingItems()
            val snackbarHostState = remember { SnackbarHostState() }

            CollectionScreen(
                state = state,
                coins = coins,
                events = viewModel.events,
                snackbarHostState = snackbarHostState,
            ) { action ->
                when (action) {
                    is CollectionScreenAction.NavigateToCoin -> {
                        parentNavController.navigate(Screen.Coin(action.coinId))
                    }

                    is CollectionScreenAction.NavigateToSet -> {
                        parentNavController.navigate(Screen.Set(action.setId))
                    }

                    else -> viewModel.onScreenAction(action)
                }
            }
        }
    }
}