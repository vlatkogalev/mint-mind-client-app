package app.presentation.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun InfoDialog(
    title: String,
    text: String,
    buttonText: String,
    imageVector: ImageVector? = null,
    modifier: Modifier = Modifier,
    onDismissAction: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissAction,
        icon = { imageVector?.let { AppDialogIcon(it) } },
        title = { AppDialogTitle(title) },
        text = { Text(text = text) },
        dismissButton = {
            TextButton(onClick = onDismissAction) {
                Text(text = buttonText)
            }
        },
        confirmButton = {},
        containerColor = AppDialogDefaults.containerColor,
        modifier = modifier
    )
}

@Preview
@Composable
private fun InfoDialogPreview() {
    AppTheme {
        InfoDialog(
            title = "Confirm",
            text = "Action is done",
            buttonText = stringResource(Res.string.ok),
            imageVector = Icons.Outlined.Lock,
            onDismissAction = {}
        )
    }
}