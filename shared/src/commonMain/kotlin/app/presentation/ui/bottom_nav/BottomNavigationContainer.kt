package app.presentation.ui.bottom_nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.core.layout.WindowSizeClass
import app.navigation.Screen
import app.navigation.graph.BottomNavGraph
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationContainer(
    navController: NavHostController,
    parentNavController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

    val bottomNavItems = listOf(Screen.Home, Screen.Collection)

    val navBarItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            bottomNavItems.forEach { item ->
                val selected = isItemSelected(item, currentDestination)

                item(
                    selected = selected,
                    onClick = { navigateToItem(navController, item) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.title)
                        )
                    },
                    label = {
                        Text(stringResource(item.title))
                    },
                    alwaysShowLabel = true,
                    colors = navBarItemColors
                )
            }
        },
        layoutType = when {
            // Tablet landscape
            windowSizeClass.isAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
            ) -> NavigationSuiteType.WideNavigationRailExpanded

            // Tablet portrait or phone landscape
            windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
            ) -> NavigationSuiteType.NavigationRail

            // Phone portrait
            else -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                currentWindowAdaptiveInfoV2()
            )
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            shortNavigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationDrawerContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            wideNavigationRailColors = WideNavigationRailDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        ),
    ) {
        BottomNavGraph(
            navController = navController,
            parentNavController = parentNavController,
            modifier = Modifier.fillMaxSize().clipToBounds()
        )
    }
}

/**
 * Checks if the navigation item is currently selected.
 */
private fun isItemSelected(
    item: Screen,
    currentDestination: NavDestination?
): Boolean {
    if (currentDestination == null) return false

    val route = currentDestination.route ?: return false
    val graphRoute = item::class.qualifiedName

    return graphRoute != null && route.contains(graphRoute)
}

/**
 * Navigates to the specified navigation item.
 */
private fun navigateToItem(
    navController: NavHostController,
    item: Screen
) {
    navController.navigate(item) {
        popUpTo(navController.graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}