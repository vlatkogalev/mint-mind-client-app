package app.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.ok
import mintmind.shared.generated.resources.redirect_text
import mintmind.shared.generated.resources.redirect_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun RedirectDialog(
    uri: String,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Outlined.OpenInBrowser,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(Res.string.redirect_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn {
                item {
                    SelectionContainer {
                        Text(text = stringResource(Res.string.redirect_text, uri))
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
                    uriHandler.openUri(uri)
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
private fun RedirectDialogPreview() {
    AppTheme {
        RedirectDialog(
            uri = "https://www.google.com",
            modifier = Modifier,
            onDismissRequest = { }
        )
    }
}