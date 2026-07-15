package collections.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.components.dialog.AppDialogDefaults
import app.presentation.components.dialog.AppDialogIcon
import app.presentation.components.dialog.AppDialogTitle
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.collection_create_set
import mintmind.shared.generated.resources.collection_set_name
import mintmind.shared.generated.resources.create
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateSetDialog(
    modifier: Modifier = Modifier,
    onConfirm: (name: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    val trimmedName = name.trim()

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { AppDialogIcon(Icons.Outlined.CreateNewFolder) },
        title = { AppDialogTitle(stringResource(Res.string.collection_create_set)) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text(text = stringResource(Res.string.collection_set_name)) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(trimmedName) },
                enabled = trimmedName.isNotEmpty()
            ) {
                Text(text = stringResource(Res.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        },
        containerColor = AppDialogDefaults.containerColor,
        modifier = modifier
    )
}

@Preview
@Composable
private fun CreateSetDialogPreview() {
    AppTheme {
        CreateSetDialog(
            onConfirm = { },
            onDismiss = { }
        )
    }
}
