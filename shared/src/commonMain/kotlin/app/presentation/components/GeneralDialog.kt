package app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme

@Composable
fun GeneralDialog(
    title: String,
    text: String,
    positiveButtonText: String,
    negativeButtonText: String,
    imageVector: ImageVector? = null,
    modifier: Modifier = Modifier,
    onConfirmAction: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            imageVector?.let { vector ->
                Icon(
                    imageVector = vector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = text)
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
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
                Text(text = positiveButtonText)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = modifier
    )
}

@Preview
@Composable
private fun GeneralDialogPreview() {
    AppTheme {
        GeneralDialog(
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