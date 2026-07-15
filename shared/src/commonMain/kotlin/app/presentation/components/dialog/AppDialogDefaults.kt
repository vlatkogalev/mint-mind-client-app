package app.presentation.components.dialog

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

object AppDialogDefaults {

    val containerColor: Color
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.surfaceContainerLowest

    val iconTint: Color
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.primary

    val destructiveColor: Color
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.error

    @Composable
    @ReadOnlyComposable
    fun accentColor(isDestructive: Boolean): Color =
        if (isDestructive) destructiveColor else iconTint
}

@Composable
fun AppDialogIcon(
    imageVector: ImageVector,
    tint: Color = AppDialogDefaults.iconTint,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun AppDialogTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}
