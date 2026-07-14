package collections.presentation.ui.collection.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material.icons.outlined.MoveDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import app.presentation.components.EmptyContent
import app.presentation.util.calculateGridConfig
import collections.domain.model.Coin
import collections.presentation.components.CoinItem
import collections.presentation.components.MultiSelectionAction
import collections.presentation.components.MultiSelectionActionBar
import collections.presentation.components.MultiSelectionContainer
import collections.presentation.ui.collection.CollectionScreenAction
import collections.presentation.ui.collection.CollectionState
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.collection_create_set
import mintmind.shared.generated.resources.collection_delete_coins_text
import mintmind.shared.generated.resources.collection_delete_coins_title
import mintmind.shared.generated.resources.collection_empty_set_desc
import mintmind.shared.generated.resources.collection_filter
import mintmind.shared.generated.resources.collection_move_item
import mintmind.shared.generated.resources.collection_move_to
import mintmind.shared.generated.resources.collection_remove_item
import mintmind.shared.generated.resources.collection_sort
import mintmind.shared.generated.resources.feed_empty_listing_text
import mintmind.shared.generated.resources.feed_empty_listing_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionAllTab(
    state: CollectionState,
    coins: LazyPagingItems<Coin>,
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
                CoinsToolbar(
                    isMultiSelectEnabled = state.isCoinMultiSelectModeEnabled,
                    onClickFilter = { /* TODO: filter */ },
                    onClickSort = { /* TODO: sort */ },
                    onToggleMultiSelect = { onAction(CollectionScreenAction.ToggleCoinMultiSelectMode) },
                )
            }

            if (coins.loadState.refresh is LoadState.Loading) {
                loadingIndicator()
            }

            coinItems(
                coins = coins,
                isMultiSelectEnabled = state.isCoinMultiSelectModeEnabled,
                selectedCoins = state.selectedCoins,
                onAction = onAction,
            )

            if (coins.loadState.refresh is LoadState.NotLoading && coins.itemSnapshotList.isEmpty()) {
                emptyState()
            }

            if (coins.loadState.append is LoadState.Loading) {
                loadingIndicator()
            }
        }

        MultiSelectionActionBar(isEnabled = state.isCoinMultiSelectModeEnabled) {
            MultiSelectionAction(
                onClick = { onAction(CollectionScreenAction.RequestDeleteSelectedCoins) },
                enabled = state.selectedCoins.isNotEmpty() && !state.isProcessingBulkAction,
                color = MaterialTheme.colorScheme.error,
                icon = Icons.Outlined.Delete,
                label = stringResource(Res.string.collection_remove_item),
            )
            MultiSelectionAction(
                onClick = { onAction(CollectionScreenAction.ShowMoveSheet) },
                enabled = state.selectedCoins.isNotEmpty() && !state.isProcessingBulkAction,
                icon = Icons.Outlined.MoveDown,
                label = stringResource(Res.string.collection_move_item),
            )
        }

        if (state.showDeleteCoinsDialog) {
            DeleteCoinsDialog(
                onConfirm = { onAction(CollectionScreenAction.ConfirmDeleteSelectedCoins) },
                onDismiss = { onAction(CollectionScreenAction.DismissDeleteDialog) },
            )
        }

        if (state.showMoveSheet) {
            MoveCoinsSheet(
                sets = state.sets,
                onSelectSet = { setId -> onAction(CollectionScreenAction.MoveSelectedCoins(setId)) },
                onDismiss = { onAction(CollectionScreenAction.DismissMoveSheet) },
                onCreateNewSet = { onAction(CollectionScreenAction.ShowCreateSetDialog) },
            )
        }
    }
}

@Composable
private fun DeleteCoinsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.collection_delete_coins_title)) },
        text = { Text(text = stringResource(Res.string.collection_delete_coins_text)) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoveCoinsSheet(
    sets: List<collections.domain.model.CoinSet>,
    onSelectSet: (String) -> Unit,
    onDismiss: () -> Unit,
    onCreateNewSet: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Text(
            text = stringResource(Res.string.collection_move_to),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        if (sets.isEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(Res.string.collection_empty_set_desc))
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    onDismiss()
                    onCreateNewSet()
                }) {
                    Text(text = stringResource(Res.string.collection_create_set))
                }
            }
        } else {
            LazyColumn {
                items(sets, key = { it.id }) { set ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSet(set.id) }
                            .padding(horizontal = 24.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = set.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${set.coinCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CoinsToolbar(
    isMultiSelectEnabled: Boolean,
    onClickFilter: () -> Unit,
    onClickSort: () -> Unit,
    onToggleMultiSelect: () -> Unit,
) {
    Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        TextButton(onClick = onClickFilter) {
            Icon(
                imageVector = Icons.Outlined.FilterAlt,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(Res.string.collection_filter))
        }

        TextButton(onClick = onClickSort) {
            Icon(
                imageVector = Icons.Outlined.FormatLineSpacing,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(Res.string.collection_sort))
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

private fun LazyGridScope.coinItems(
    coins: LazyPagingItems<Coin>,
    isMultiSelectEnabled: Boolean,
    selectedCoins: Set<Coin>,
    onAction: (CollectionScreenAction) -> Unit,
) {
    items(count = coins.itemCount, key = coins.itemKey { it.id }) { index ->
        val coin = coins[index]
        if (coin != null) {
            MultiSelectionContainer(
                isEnabled = isMultiSelectEnabled,
                isSelected = coin in selectedCoins,
                onClick = {
                    if (isMultiSelectEnabled) {
                        onAction(CollectionScreenAction.ToggleCoinSelected(coin))
                    } else {
                        onAction(CollectionScreenAction.NavigateToCoin(coin.id))
                    }
                },
                onCheckedChange = { onAction(CollectionScreenAction.ToggleCoinSelected(coin)) },
                modifier = Modifier.width(320.dp).animateItem(),
            ) {
                CoinItem(
                    coin = coin,
                    modifier = Modifier,
                    onClick = {
                        if (isMultiSelectEnabled) {
                            onAction(CollectionScreenAction.ToggleCoinSelected(coin))
                        } else {
                            onAction(CollectionScreenAction.NavigateToCoin(coin.id))
                        }
                    },
                )
            }
        }
    }
}

private fun LazyGridScope.loadingIndicator() {
    item(span = { GridItemSpan(maxLineSpan) }) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

private fun LazyGridScope.emptyState() {
    item(span = { GridItemSpan(maxLineSpan) }) {
        EmptyContent(
            icon = Icons.AutoMirrored.Outlined.Feed,
            title = Res.string.feed_empty_listing_title,
            text = Res.string.feed_empty_listing_text,
            modifier = Modifier
        )
    }
}
