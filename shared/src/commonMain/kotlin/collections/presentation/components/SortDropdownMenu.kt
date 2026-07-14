package collections.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> SortDropdownMenu(
    expanded: Boolean,
    options: List<T>,
    selected: T,
    labelOf: (T) -> StringResource,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(text = stringResource(labelOf(option))) },
                onClick = {
                    onSelect(option)
                    onDismiss()
                },
                trailingIcon = {
                    if (option == selected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    }
}
