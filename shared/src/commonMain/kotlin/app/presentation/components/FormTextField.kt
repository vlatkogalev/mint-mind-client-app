package app.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.required
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FormTextField(
    hint: String,
    value: String,
    error: String? = null,
    isRequired: Boolean = false,
    maxChar: Int = Int.MAX_VALUE,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    helperText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    minLines: Int = 1,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onFormEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxChar) {
                onFormEvent(it)
            }
        },
        label = {
            Text(
                text = hint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        prefix = prefix,
        suffix = suffix,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = error != null,
        supportingText = {
            when {
                error != null -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                isRequired -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.required),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                helperText != null -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = helperText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> Unit
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            autoCorrectEnabled = false,
            keyboardType = keyboardType
        ),
        singleLine = singleLine,
        minLines = minLines,
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun FormTextFieldPreview() {
    AppTheme {
        FormTextField(
            hint = "Username",
            value = "john.doe",
            isRequired = true,
            onFormEvent = {}
        )
    }
}