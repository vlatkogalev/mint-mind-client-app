package app.presentation.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Extension modifier that applies padding considering device cutouts.
 * This automatically handles safe areas with display cutouts (like notches)
 * and applies proper padding for all sides.
 */
@Composable
fun Modifier.cutoutAwarePadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
    applyStartCutoutPadding: Boolean = true
): Modifier {
    val layoutDirection = LocalLayoutDirection.current
    val insets = WindowInsets.displayCutout.asPaddingValues()

    val safeStart = insets.calculateStartPadding(layoutDirection) + start
    val safeEnd = insets.calculateEndPadding(layoutDirection) + end

    return this.padding(
        start = if (applyStartCutoutPadding) safeStart else start,
        top = top,
        end = safeEnd,
        bottom = bottom
    )
}