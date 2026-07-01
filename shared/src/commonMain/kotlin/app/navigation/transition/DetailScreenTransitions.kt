package app.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private const val NAV_ANIMATION_DURATION = 200

/**
 * Navigation transitions for detail screens (horizontal slide)
 */
object DetailScreenTransitions {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(
                durationMillis = NAV_ANIMATION_DURATION,
                easing = LinearEasing
            )
        )
    }
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(
                durationMillis = NAV_ANIMATION_DURATION,
                easing = LinearEasing
            ),
            targetOffset = { fullOffset -> (fullOffset * 0.3f).toInt() }
        )
    }
    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = NAV_ANIMATION_DURATION,
                    easing = LinearEasing
                ),
                initialOffset = { fullOffset -> (fullOffset * 0.3f).toInt() }
            )
        }
    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = NAV_ANIMATION_DURATION,
                    easing = LinearEasing
                )
            )
        }
}