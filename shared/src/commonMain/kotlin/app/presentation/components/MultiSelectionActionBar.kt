package app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.presentation.util.DeviceOrientation
import app.presentation.util.getDeviceOrientation

@Composable
fun BoxScope.MultiSelectionActionBar(
    isEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceOrientation = windowSizeClass.getDeviceOrientation()
    val systemBottomPadding = if (deviceOrientation == DeviceOrientation.LANDSCAPE) 16.dp else 0.dp

    AnimatedVisibility(
        visible = isEnabled,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 24.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .background(MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.1f))
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp + systemBottomPadding)
        ) {
            content()
        }
    }
}
