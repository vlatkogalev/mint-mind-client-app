package app.presentation.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates PaddingValues that considers display cutouts for horizontal padding.
 * Adds specified padding values on top of safe insets.
 */
@Composable
fun cutoutAwarePaddingValues(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
    applyStartCutoutPadding: Boolean = true
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    val insets = WindowInsets.displayCutout.asPaddingValues()

    val safeStart = insets.calculateStartPadding(layoutDirection) + start
    val safeEnd = insets.calculateEndPadding(layoutDirection) + end

    return PaddingValues(
        start = if (applyStartCutoutPadding) safeStart else start,
        top = top,
        end = safeEnd,
        bottom = bottom
    )
}