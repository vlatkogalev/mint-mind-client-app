package app.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import app.presentation.util.cutoutAwarePadding

/**
 * A reusable bottom bar button for wizard screens.
 *
 * Features:
 * - Background always spans full width for a consistent bar appearance
 * - Button width controlled by [widthFraction] and centered (use 0.5f on large landscape screens)
 * - Consistent padding with cutout awareness
 *
 * @param text Button label text
 * @param onClick Callback when button is clicked
 * @param widthFraction Fraction of the bar width the button should occupy (default 1f = full width)
 * @param modifier Modifier for the root Box container
 */
@Composable
fun BottomBarButton(
    text: String,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    widthFraction: Float = 1f,
    modifier: Modifier = Modifier,
) {
    val insets = WindowInsets.navigationBars.asPaddingValues()
    val safeBottom = insets.calculateBottomPadding() + 8.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .cutoutAwarePadding(start = 24.dp, top = 8.dp, end = 24.dp, bottom = safeBottom)
    ) {
        PrimaryButton(
            isLoading = isLoading,
            text = text,
            onButtonClick = onClick,
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun BottomBarButtonPreview() {
    AppTheme {
        BottomBarButton(
            text = "Submit",
            onClick = { }
        )
    }
}
