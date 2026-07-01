package app.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.navigation.Screen
import app.navigation.screens.authScreens
import app.navigation.screens.collectionScreens
import app.navigation.screens.feedScreens
import app.navigation.screens.identifyScreens
import app.navigation.screens.userScreens
import app.navigation.transition.DetailScreenTransitions
import app.presentation.ui.bottom_nav.BottomNavigationContainer
import app.presentation.ui.onboarding.OnboardingScreen
import app.presentation.ui.onboarding.OnboardingScreenAction
import app.presentation.ui.onboarding.OnboardingViewModel
import app.util.coreComponent
import kotlinx.coroutines.flow.first
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppGraph() {
    var startDestination by remember { mutableStateOf<Screen?>(null) }

    LaunchedEffect(Unit) {
        val completed = coreComponent.appPreferences.hasCompletedOnboarding().first()
        startDestination = if (completed) Screen.BottomNav else Screen.Onboarding
    }

    val resolvedStart = startDestination ?: return

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = resolvedStart,
        enterTransition = DetailScreenTransitions.enterTransition,
        exitTransition = DetailScreenTransitions.exitTransition,
        popEnterTransition = DetailScreenTransitions.popEnterTransition,
        popExitTransition = DetailScreenTransitions.popExitTransition
    ) {
        composable<Screen.Onboarding> {
            val viewModel = koinViewModel<OnboardingViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            OnboardingScreen(
                state = state
            ) { action ->
                when (action) {
                    OnboardingScreenAction.NavigateToHome -> {
                        navController.navigate(Screen.BottomNav) {
                            viewModel.completeOnboarding()
                            popUpTo(Screen.Onboarding) { inclusive = true }
                        }
                    }

                    OnboardingScreenAction.NavigateToLogin -> {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Onboarding) { inclusive = true }
                        }
                    }

                    else -> viewModel.onScreenAction(action)
                }
            }
        }

        composable<Screen.BottomNav> {
            val mainNavController = rememberNavController()

            BottomNavigationContainer(
                navController = mainNavController,
                parentNavController = navController
            )
        }

        authScreens(navController)
        identifyScreens(navController)
        collectionScreens(navController)
        feedScreens(navController)
        userScreens(navController)
    }
}
