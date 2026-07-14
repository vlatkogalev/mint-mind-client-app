package collections.presentation.ui.collection.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.presentation.components.EmptyContent
import app.presentation.util.calculateGridConfig
import collections.domain.model.CoinSet
import collections.domain.model.CoinSetSortOption
import collections.presentation.components.MultiSelectionAction
import collections.presentation.components.MultiSelectionActionBar
import collections.presentation.components.MultiSelectionContainer
import collections.presentation.components.SetItem
import collections.presentation.components.SortDropdownMenu
import collections.presentation.ui.collection.CollectionScreenAction
import collections.presentation.ui.collection.CollectionState
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.collection_create_set
import mintmind.shared.generated.resources.collection_delete_sets_text
import mintmind.shared.generated.resources.collection_delete_sets_title
import mintmind.shared.generated.resources.collection_empty_set_desc
import mintmind.shared.generated.resources.collection_empty_set_title
import mintmind.shared.generated.resources.collection_remove_item
import mintmind.shared.generated.resources.collection_sort
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionSetsTab(
    state: CollectionState,
    onAction: (CollectionScreenAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = gridConfig.gridCells,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                SetsHeader(
                    isMultiSelectEnabled = state.isSetMultiSelectModeEnabled,
                    onClickCreateNewSet = { onAction(CollectionScreenAction.ShowCreateSetDialog) },
                    setSortOption = state.setSortOption,
                    onChangeSetSort = { onAction(CollectionScreenAction.ChangeSetSort(it)) },
                    onToggleMultiSelect = { onAction(CollectionScreenAction.ToggleSetMultiSelectMode) },
                )
            }

            if (state.sets.isEmpty()) {
                emptyState()
            } else {
                setItems(
                    sets = state.sets,
                    isMultiSelectEnabled = state.isSetMultiSelectModeEnabled,
                    selectedSets = state.selectedSets,
                    onAction = onAction,
                )
            }
        }

        MultiSelectionActionBar(isEnabled = state.isSetMultiSelectModeEnabled) {
            MultiSelectionAction(
                onClick = { onAction(CollectionScreenAction.RequestDeleteSelectedSets) },
                enabled = state.selectedSets.isNotEmpty() && !state.isProcessingBulkAction,
                color = MaterialTheme.colorScheme.error,
                icon = Icons.Outlined.Delete,
                label = stringResource(Res.string.collection_remove_item),
            )
        }

        if (state.showDeleteSetsDialog) {
            DeleteSetsDialog(
                onConfirm = { onAction(CollectionScreenAction.ConfirmDeleteSelectedSets) },
                onDismiss = { onAction(CollectionScreenAction.DismissDeleteDialog) },
            )
        }
    }
}

@Composable
private fun DeleteSetsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.collection_delete_sets_title)) },
        text = { Text(text = stringResource(Res.string.collection_delete_sets_text)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(Res.string.collection_remove_item),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
    )
}

@Composable
private fun SetsHeader(
    isMultiSelectEnabled: Boolean,
    onClickCreateNewSet: () -> Unit,
    setSortOption: CoinSetSortOption,
    onChangeSetSort: (CoinSetSortOption) -> Unit,
    onToggleMultiSelect: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        CreateSetButton(
            onClick = onClickCreateNewSet,
        )
        SetsToolbar(
            isMultiSelectEnabled = isMultiSelectEnabled,
            setSortOption = setSortOption,
            onChangeSetSort = onChangeSetSort,
            onToggleMultiSelect = onToggleMultiSelect,
        )
    }
}

@Composable
private fun CreateSetButton(
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Outlined.CreateNewFolder,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(Res.string.collection_create_set))
    }
}

@Composable
private fun SetsToolbar(
    isMultiSelectEnabled: Boolean,
    setSortOption: CoinSetSortOption,
    onChangeSetSort: (CoinSetSortOption) -> Unit,
    onToggleMultiSelect: () -> Unit,
) {
    var sortExpanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
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
                options = CoinSetSortOption.entries,
                selected = setSortOption,
                labelOf = { it.label },
                onSelect = onChangeSetSort,
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

private fun LazyGridScope.emptyState() {
    item(span = { GridItemSpan(maxLineSpan) }) {
        EmptyContent(
            icon = Icons.Outlined.CreateNewFolder,
            title = Res.string.collection_empty_set_title,
            text = Res.string.collection_empty_set_desc,
            modifier = Modifier
        )
    }
}

private fun LazyGridScope.setItems(
    sets: List<CoinSet>,
    isMultiSelectEnabled: Boolean,
    selectedSets: Set<CoinSet>,
    onAction: (CollectionScreenAction) -> Unit,
) {
    items(
        count = sets.size,
        key = { index -> sets[index].id }
    ) { index ->
        val set = sets[index]
        MultiSelectionContainer(
            isEnabled = isMultiSelectEnabled,
            isSelected = set in selectedSets,
            onClick = {
                if (isMultiSelectEnabled) {
                    onAction(CollectionScreenAction.ToggleSetSelected(set))
                } else {
                    onAction(CollectionScreenAction.NavigateToSet(set.id))
                }
            },
            onCheckedChange = { onAction(CollectionScreenAction.ToggleSetSelected(set)) },
            modifier = Modifier.width(320.dp).animateItem(),
        ) {
            SetItem(
                set = set,
                modifier = Modifier,
                onClick = {
                    if (isMultiSelectEnabled) {
                        onAction(CollectionScreenAction.ToggleSetSelected(set))
                    } else {
                        onAction(CollectionScreenAction.NavigateToSet(set.id))
                    }
                },
            )
        }
    }
}
