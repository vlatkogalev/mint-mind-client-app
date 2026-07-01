package auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.required
import org.jetbrains.compose.resources.stringResource

@Composable
fun PasswordTextField(
    hint: String,
    value: String,
    error: String? = null,
    isRequired: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onFormEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onFormEvent,
        label = { Text(text = hint) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = leadingIcon,
        trailingIcon = {
            val (icon, iconDescription) = if (showPassword) {
                Pair(Icons.Filled.VisibilityOff, "Hide password")
            } else {
                Pair(Icons.Filled.Visibility, "Show password")
            }

            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    imageVector = icon,
                    contentDescription = iconDescription
                )
            }
        },
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

                else -> Unit
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
    )
}