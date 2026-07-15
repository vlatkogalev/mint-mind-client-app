package collections.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import collections.domain.model.CoinSortOption
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_filter
import mintmind.shared.generated.resources.collection_sort
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoinsToolbar(
    isMultiSelectEnabled: Boolean,
    onClickFilter: () -> Unit,
    coinSortOption: CoinSortOption,
    onChangeSort: (CoinSortOption) -> Unit,
    onToggleMultiSelect: () -> Unit,
) {
    var sortExpanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        TextButton(onClick = onClickFilter) {
            Icon(
                imageVector = Icons.Outlined.FilterAlt,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(Res.string.collection_filter))
        }

        Box {
            TextButton(onClick = { sortExpanded = true }) {
                Icon(
                    imageVector = Icons.Outlined.FormatLineSpacing,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(Res.string.collection_sort))
            }

            SortDropdownMenu(
                expanded = sortExpanded,
                options = CoinSortOption.entries,
                selected = coinSortOption,
                labelOf = { it.label },
                onSelect = onChangeSort,
                onDismiss = { sortExpanded = false },
            )
        }

        Spacer(Modifier.weight(1f))

        FilledIconToggleButton(
            checked = isMultiSelectEnabled,
            onCheckedChange = { onToggleMultiSelect() },
            colors = IconButtonDefaults.filledIconToggleButtonColors().copy(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Checklist,
                contentDescription = null,
                tint = if (isMultiSelectEnabled) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}
