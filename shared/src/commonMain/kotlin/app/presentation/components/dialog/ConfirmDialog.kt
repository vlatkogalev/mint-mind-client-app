package app.presentation.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    positiveButtonText: String,
    negativeButtonText: String,
    imageVector: ImageVector? = null,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier,
    onConfirmAction: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val accent = AppDialogDefaults.accentColor(isDestructive)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = { imageVector?.let { AppDialogIcon(imageVector = it, tint = accent) } },
        title = { AppDialogTitle(title) },
        text = { Text(text = text) },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = negativeButtonText)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmAction()
                    onDismissRequest()
                }
            ) {
                Text(
                    text = positiveButtonText,
                    color = if (isDestructive) accent else Color.Unspecified
                )
            }
        },
        containerColor = AppDialogDefaults.containerColor,
        modifier = modifier
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    AppTheme {
        ConfirmDialog(
            title = "Logout",
            text = "Are you sure you want to logout?",
            positiveButtonText = "Logout",
            negativeButtonText = "Cancel",
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            onConfirmAction = {},
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun ConfirmDialogDestructivePreview() {
    AppTheme {
        ConfirmDialog(
            title = "Delete",
            text = "Are you sure you want to delete this?",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            imageVector = Icons.Outlined.Delete,
            isDestructive = true,
            onConfirmAction = {},
            onDismissRequest = {}
        )
    }
}