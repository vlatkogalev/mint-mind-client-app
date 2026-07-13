package collections.presentation.ui.collection.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.presentation.util.calculateGridConfig
import collections.domain.model.CoinSet
import collections.presentation.ui.collection.CollectionState
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_create_set
import mintmind.shared.generated.resources.collection_sort
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionSetsTab(
    state: CollectionState,
    onClickCreateNewSet: () -> Unit,
    onSelectSet: (id: String) -> Unit,
    onCheckSet: (set: CoinSet) -> Unit,
    onClickSort: () -> Unit,
    onDeleteSelectedSets: () -> Unit,
    onSetMultiSelectionModeEnabled: () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    LazyVerticalGrid(
        columns = gridConfig.gridCells,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            val contentPadding = if (gridConfig.gridCells == GridCells.Fixed(1)) {
                ButtonDefaults.ContentPadding
            } else {
                PaddingValues(horizontal = 96.dp)
            }

            val buttonModifier = if (gridConfig.gridCells == GridCells.Fixed(1)) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.wrapContentWidth()
            }

            TextButton(
                onClick = { onClickCreateNewSet() },
                contentPadding = contentPadding,
                modifier = buttonModifier
            ) {
                Icon(
                    imageVector = Icons.Outlined.CreateNewFolder,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(Res.string.collection_create_set))
            }
        }

        stickyHeader {
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                TextButton(onClick = { onClickSort() }) {
                    Icon(
                        imageVector = Icons.Outlined.FormatLineSpacing,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(Res.string.collection_sort))
                }

                Spacer(Modifier.weight(1f))

                FilledIconToggleButton(
                    checked = state.isSetMultiSelectModeEnabled,
                    onCheckedChange = { onSetMultiSelectionModeEnabled() },
                    colors = IconButtonDefaults.filledIconToggleButtonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Checklist,
                        contentDescription = null,
                        tint = if (state.isSetMultiSelectModeEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}