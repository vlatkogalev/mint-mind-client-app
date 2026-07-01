package app.presentation.util

import androidx.window.core.layout.WindowSizeClass

fun WindowSizeClass.calculateAdaptiveWidth(
    compactFraction: Float = 0.85f,
    mediumFraction: Float = 0.5f,
    expandedFraction: Float = 0.35f
): Float {
    return when {
        // Tablet landscape (width >= 840dp, height >= 480dp)
        isAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        ) -> expandedFraction

        // Tablet portrait or phone landscape (width >= 600dp)
        isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> mediumFraction

        // Phone portrait (width < 600dp)
        else -> compactFraction
    }
}