package collections.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.collection_create_set
import mintmind.shared.generated.resources.collection_set_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateSetDialog(
    onConfirm: (name: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.collection_create_set)) },
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
                onClick = { onConfirm(name.trim()) },
                enabled = name.isNotBlank()
            ) {
                Text(text = stringResource(Res.string.collection_create_set))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
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
