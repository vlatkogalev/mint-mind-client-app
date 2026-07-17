package collections.presentation.ui.set

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PlaylistRemove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import app.presentation.components.AppTopBar
import app.presentation.components.EmptyContent
import app.presentation.components.TopAppBarText
import app.presentation.components.dialog.ConfirmDialog
import app.presentation.theme.AppTheme
import app.presentation.util.calculateGridConfig
import app.presentation.util.cutoutAwarePadding
import collections.domain.model.Coin
import collections.presentation.components.CoinItem
import collections.presentation.components.CoinsToolbar
import collections.presentation.components.MultiSelectionAction
import collections.presentation.components.MultiSelectionActionBar
import collections.presentation.components.MultiSelectionContainer
import collections.presentation.components.StatsToolbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.cancel
import mintmind.shared.generated.resources.collection_coins_deleted
import mintmind.shared.generated.resources.collection_delete_coins_text
import mintmind.shared.generated.resources.collection_delete_coins_title
import mintmind.shared.generated.resources.collection_remove_from_set
import mintmind.shared.generated.resources.collection_remove_from_set_text
import mintmind.shared.generated.resources.collection_remove_from_set_title
import mintmind.shared.generated.resources.collection_remove_item
import mintmind.shared.generated.resources.collection_set_coins_deleted
import mintmind.shared.generated.resources.collection_set_empty_desc
import mintmind.shared.generated.resources.collection_set_empty_title
import mintmind.shared.generated.resources.set_total_value
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetScreen(
    state: SetState,
    coins: LazyPagingItems<Coin>,
    events: Flow<SetScreenEvent>,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (SetScreenAction) -> Unit,
) {
    var currentEvent by remember { mutableStateOf<SetScreenEvent?>(null) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SetScreenEvent.RefreshCoins -> coins.refresh()
                else -> currentEvent = event
            }
        }
    }

    currentEvent?.let { event ->
        val message = when (event) {
            is SetScreenEvent.CoinsRemovedFromSet -> {
                pluralStringResource(
                    Res.plurals.collection_set_coins_deleted,
                    event.count,
                    event.count
                )
            }

            is SetScreenEvent.CoinsDeleted -> {
                pluralStringResource(
                    Res.plurals.collection_coins_deleted,
                    event.count,
                    event.count
                )
            }

            is SetScreenEvent.Error -> "An error occurred. Please try again."

            is SetScreenEvent.BulkActionBlocked -> event.reason

            is SetScreenEvent.RefreshCoins -> ""
        }

        LaunchedEffect(event) {
            snackbarHostState.showSnackbar(message)
            currentEvent = null
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    TopAppBarText(text = state.set?.name ?: "")
                },
                onNavigateUp = { onScreenAction(SetScreenAction.NavigateUp) },
                transparent = true,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        MainContent(
            state = state,
            coins = coins,
            paddingValues = paddingValues,
            onScreenAction = onScreenAction,
        )
    }
}

@Composable
private fun MainContent(
    state: SetState,
    coins: LazyPagingItems<Coin>,
    paddingValues: PaddingValues,
    onScreenAction: (action: SetScreenAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .cutoutAwarePadding(
                top = paddingValues.calculateTopPadding(),
                applyStartCutoutPadding = false
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        StatsToolbar(
            title = stringResource(Res.string.set_total_value),
            totalValue = state.set?.totalValue ?: 0.0,
            objectCount = state.set?.coinCount ?: 0,
            issuerCount = state.set?.issuerCount ?: 0,
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                        onChangeSort = { onScreenAction(SetScreenAction.ChangeCoinSort(it)) },
                        onToggleMultiSelect = { onScreenAction(SetScreenAction.ToggleCoinMultiSelectMode) },
                    )
                }

                if (coins.loadState.refresh is LoadState.Loading) {
                    loadingIndicator()
                }

                coinItems(
                    coins = coins,
                    isMultiSelectEnabled = state.isCoinMultiSelectModeEnabled,
                    selectedCoins = state.selectedCoins,
                    onScreenAction = onScreenAction,
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
                    onClick = { onScreenAction(SetScreenAction.RequestRemoveSelectedFromSet) },
                    enabled = state.selectedCoins.isNotEmpty() && !state.isProcessingBulkAction,
                    icon = Icons.Outlined.PlaylistRemove,
                    label = stringResource(Res.string.collection_remove_from_set),
                )
                MultiSelectionAction(
                    onClick = { onScreenAction(SetScreenAction.RequestDeleteSelectedCoins) },
                    enabled = state.selectedCoins.isNotEmpty() && !state.isProcessingBulkAction,
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Outlined.Delete,
                    label = stringResource(Res.string.collection_remove_item),
                )
            }

            if (state.showRemoveFromSetDialog) {
                ConfirmDialog(
                    title = stringResource(Res.string.collection_remove_from_set_title),
                    text = stringResource(Res.string.collection_remove_from_set_text),
                    positiveButtonText = stringResource(Res.string.collection_remove_from_set),
                    negativeButtonText = stringResource(Res.string.cancel),
                    imageVector = Icons.Outlined.PlaylistRemove,
                    onConfirmAction = { onScreenAction(SetScreenAction.ConfirmRemoveSelectedFromSet) },
                    onDismissRequest = { onScreenAction(SetScreenAction.DismissDialog) },
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
                    onConfirmAction = { onScreenAction(SetScreenAction.ConfirmDeleteSelectedCoins) },
                    onDismissRequest = { onScreenAction(SetScreenAction.DismissDialog) },
                )
            }
        }
    }
}

private fun LazyGridScope.coinItems(
    coins: LazyPagingItems<Coin>,
    isMultiSelectEnabled: Boolean,
    selectedCoins: Set<Coin>,
    onScreenAction: (SetScreenAction) -> Unit,
) {
    items(count = coins.itemCount, key = coins.itemKey { it.id }) { index ->
        val coin = coins[index]
        if (coin != null) {
            MultiSelectionContainer(
                isEnabled = isMultiSelectEnabled,
                isSelected = coin in selectedCoins,
                onClick = {
                    if (isMultiSelectEnabled) {
                        onScreenAction(SetScreenAction.ToggleCoinSelected(coin))
                    } else {
                        onScreenAction(SetScreenAction.NavigateToCoin(coin.id))
                    }
                },
                onCheckedChange = { onScreenAction(SetScreenAction.ToggleCoinSelected(coin)) },
                modifier = Modifier.width(320.dp).animateItem(),
            ) {
                CoinItem(
                    coin = coin,
                    modifier = Modifier,
                    onClick = {
                        if (isMultiSelectEnabled) {
                            onScreenAction(SetScreenAction.ToggleCoinSelected(coin))
                        } else {
                            onScreenAction(SetScreenAction.NavigateToCoin(coin.id))
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
            title = Res.string.collection_set_empty_title,
            text = Res.string.collection_set_empty_desc,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun SetScreenPreview() {
    val samplePagingData = PagingData.from(Coin.dummyItemsList(3))
    val flow = MutableStateFlow(samplePagingData)
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    AppTheme {
        SetScreen(
            state = SetState(),
            coins = lazyPagingItems,
            events = emptyFlow(),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}
