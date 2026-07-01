package app.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import app.navigation.Screen
import feed.presentation.ui.coin_listings.CoinListingsScreen
import feed.presentation.ui.coin_listings.CoinListingsScreenAction
import feed.presentation.ui.coin_listings.CoinListingsViewModel
import feed.presentation.ui.news_feed.NewsFeedScreen
import feed.presentation.ui.news_feed.NewsFeedScreenAction
import feed.presentation.ui.news_feed.NewsFeedViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.feedScreens(
    navController: NavController
) {
    composable<Screen.NewsFeed> {
        val viewModel = koinViewModel<NewsFeedViewModel>()
        val state by viewModel.state.collectAsState()
        val posts = viewModel.postsPagingFlow.collectAsLazyPagingItems()
        val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
        val snackbarHostState = remember { SnackbarHostState() }

        NewsFeedScreen(
            state = state,
            posts = posts,
            windowSizeClass = windowSizeClass,
            snackbarHostState = snackbarHostState,
        ) { action ->
            when (action) {
                NewsFeedScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                is NewsFeedScreenAction.ToggleRedirectDialog -> {
                    viewModel.toggleRedirectDialog(action.redirectUri)
                }

                is NewsFeedScreenAction.LoadPost -> {
                    action.url?.let { uri -> viewModel.toggleRedirectDialog(uri) }
                }
            }
        }
    }

    composable<Screen.CoinListings> {
        val viewModel = koinViewModel<CoinListingsViewModel>()
        val state by viewModel.state.collectAsState()
        val coinListings = viewModel.coinListingsPagingFlow.collectAsLazyPagingItems()
        val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
        val snackbarHostState = remember { SnackbarHostState() }

        CoinListingsScreen(
            state = state,
            coinListings = coinListings,
            windowSizeClass = windowSizeClass,
            snackbarHostState = snackbarHostState,
        ) { action ->
            when (action) {
                CoinListingsScreenAction.NavigateUp -> {
                    navController.navigateUp()
                }

                is CoinListingsScreenAction.ToggleRedirectDialog -> {
                    viewModel.toggleRedirectDialog(action.redirectUri)
                }

                is CoinListingsScreenAction.LoadCoinListing -> {
                    action.url?.let { uri -> viewModel.toggleRedirectDialog(uri) }
                }
            }
        }
    }
}