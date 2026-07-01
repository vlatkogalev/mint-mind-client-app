package app.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomDropdownMenu(
    label: String,
    searchTerms: Map<T, String>,
    value: String,
    error: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            FormTextField(
                hint = label,
                value = value,
                error = error,
                isRequired = isRequired,
                onFormEvent = { },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                enabled = isEnabled,
                readOnly = true,
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                onDismissRequest = { expanded = false }
            ) {
                for ((key, name) in searchTerms) {
                    DropdownMenuItem(
                        text = { Text(text = name) },
                        onClick = {
                            onItemSelected(key)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomDropdownMenuPreview() {
    AppTheme {
        CustomDropdownMenu(
            label = "Status",
            searchTerms = mapOf(
                "all" to "All",
                "active" to "Active",
                "inactive" to "Inactive",
                "pending" to "Pending"
            ),
            value = "Active",
            isRequired = true,
            onItemSelected = {}
        )
    }
}