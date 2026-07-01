package app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
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
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    buttonText: String,
    imageVector: ImageVector? = null,
    modifier: Modifier = Modifier,
    onDismissAction: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissAction,
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
            TextButton(onClick = { onDismissAction() }) {
                Text(text = buttonText)
            }
        },
        confirmButton = {},
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = modifier
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    AppTheme {
        ConfirmDialog(
            title = "Confirm",
            text = "Action is done",
            buttonText = stringResource(Res.string.ok),
            imageVector = Icons.Outlined.Lock,
            onDismissAction = {}
        )
    }
}