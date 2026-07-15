package collections.presentation.ui.collection.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoveDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import app.presentation.components.EmptyContent
import app.presentation.components.dialog.ConfirmDialog
import app.presentation.util.calculateGridConfig
import collections.domain.model.Coin
import collections.presentation.components.CoinItem
import collections.presentation.components.CoinsToolbar
import collections.presentation.components.MoveToSetSheet
import collections.presentation.components.MultiSelectionAction
import collections.presentation.components.MultiSelectionActionBar
import collections.presentation.components.MultiSelectionContainer
import collections.presentation.ui.collection.CollectionScreenAction
import collections.presentation.ui.collection.CollectionState
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.collection_delete_coins_text
import mintmind.shared.generated.resources.collection_delete_coins_title
import mintmind.shared.generated.resources.collection_move_item
import mintmind.shared.generated.resources.collection_remove_item
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
                    coinSortOption = state.coinSortOption,
                    onChangeSort = { onAction(CollectionScreenAction.ChangeCoinSort(it)) },
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
            ConfirmDialog(
                title = stringResource(Res.string.collection_delete_coins_title),
                text = stringResource(Res.string.collection_delete_coins_text),
                positiveButtonText = stringResource(Res.string.collection_remove_item),
                negativeButtonText = stringResource(Res.string.cancel),
                imageVector = Icons.Outlined.Delete,
                isDestructive = true,
                onConfirmAction = { onAction(CollectionScreenAction.ConfirmDeleteSelectedCoins) },
                onDismissRequest = { onAction(CollectionScreenAction.DismissDeleteDialog) },
            )
        }

        if (state.showMoveSheet) {
            MoveToSetSheet(
                sets = state.sets,
                onSelectSet = { setId -> onAction(CollectionScreenAction.MoveSelectedCoins(setId)) },
                onDismiss = { onAction(CollectionScreenAction.DismissMoveSheet) },
                onCreateNewSet = { onAction(CollectionScreenAction.ShowCreateSetDialog) },
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
