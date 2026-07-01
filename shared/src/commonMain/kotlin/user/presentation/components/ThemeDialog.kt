package user.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import app.domain.Theme
import app.presentation.components.RadioButtonWithLabel
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.ok
import mintmind.shared.generated.resources.settings_theme_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ThemeDialog(
    initialTheme: Theme?,
    modifier: Modifier = Modifier,
    onConfirmed: (theme: Theme) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedTheme by rememberSaveable { mutableStateOf(initialTheme) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(Res.string.settings_theme_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Theme.entries.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButtonWithLabel(
                            text = stringResource(option.title),
                            selected = selectedTheme == option,
                            onSelect = { selectedTheme = option }
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(stringResource(Res.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmed(selectedTheme ?: Theme.System)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok))
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = modifier
    )
}

@Preview
@Composable
private fun ThemeDialogPreview() {
    AppTheme {
        ThemeDialog(
            initialTheme = Theme.System,
            modifier = Modifier,
            onConfirmed = { },
            onDismissRequest = { }
        )
    }
}