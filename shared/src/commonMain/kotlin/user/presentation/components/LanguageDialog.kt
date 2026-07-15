package user.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.domain.Language
import app.presentation.components.RadioButtonWithLabel
import app.presentation.components.dialog.AppDialogDefaults
import app.presentation.components.dialog.AppDialogIcon
import app.presentation.components.dialog.AppDialogTitle
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.ok
import mintmind.shared.generated.resources.settings_language_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageDialog(
    initialLanguage: Language?,
    modifier: Modifier = Modifier,
    onConfirmed: (language: Language) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedLanguage by rememberSaveable { mutableStateOf(initialLanguage) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = { AppDialogIcon(Icons.Outlined.Translate) },
        title = { AppDialogTitle(stringResource(Res.string.settings_language_dialog_title)) },
        text = {
            LazyColumn {
                items(Language.entries, key = { it.name }) { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButtonWithLabel(
                            text = stringResource(option.title),
                            selected = selectedLanguage == option,
                            onSelect = { selectedLanguage = option }
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
                    onConfirmed(selectedLanguage ?: Language.English)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok))
            }
        },
        containerColor = AppDialogDefaults.containerColor,
        modifier = modifier
    )
}

@Preview
@Composable
private fun LanguageDialogPreview() {
    AppTheme {
        LanguageDialog(
            initialLanguage = Language.English,
            modifier = Modifier,
            onConfirmed = { },
            onDismissRequest = { }
        )
    }
}