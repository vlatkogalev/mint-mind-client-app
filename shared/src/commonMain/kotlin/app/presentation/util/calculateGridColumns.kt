package app.presentation.util

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.window.core.layout.WindowSizeClass

data class GridConfig(
    val gridCells: GridCells,
    val columnCount: Int
)

fun WindowSizeClass.calculateGridConfig(
    compactCells: Int = 2,
    mediumCells: Int = 3,
    expandedCells: Int = 4,
): GridConfig {
    val columnCount = when {
        // Tablet landscape (width >= 840dp, height >= 480dp)
        isAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        ) -> expandedCells

        // Tablet portrait or phone landscape (width >= 600dp)
        isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> mediumCells

        // Phone portrait (width < 600dp)
        else -> compactCells
    }

    return GridConfig(GridCells.Fixed(columnCount), columnCount)
}

fun WindowSizeClass.calculateStaggeredGridCells(
    compactCells: Int = 1,
    mediumCells: Int = 2,
    expandedCells: Int = 3,
): StaggeredGridCells {
    val columnCount = when {
        // Tablet landscape
        isAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        ) -> expandedCells

        // Tablet portrait or phone landscape
        isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> mediumCells

        // Phone portrait
        else -> compactCells
    }

    return StaggeredGridCells.Fixed(columnCount)
}