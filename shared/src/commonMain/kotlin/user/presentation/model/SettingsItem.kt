package user.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

data class SettingsItem(
    val title: StringResource,
    val supportingText: String? = null,
    val leadingIcon: ImageVector,
    val onClick: () -> Unit
)