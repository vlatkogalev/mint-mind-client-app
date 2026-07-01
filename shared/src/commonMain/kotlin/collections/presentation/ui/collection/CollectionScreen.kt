package collections.presentation.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.FormatLineSpacing
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import app.presentation.components.EmptyContent
import app.presentation.theme.AppTheme
import app.presentation.util.calculateGridConfig
import app.presentation.util.cutoutAwarePadding
import collections.domain.model.Coin
import collections.domain.model.CollectionScreenType
import collections.presentation.components.CoinItem
import collections.presentation.components.StatsToolbar
import kotlinx.coroutines.flow.MutableStateFlow
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_filter
import mintmind.shared.generated.resources.collection_sort
import mintmind.shared.generated.resources.collection_total_value
import mintmind.shared.generated.resources.feed_empty_listing_text
import mintmind.shared.generated.resources.feed_empty_listing_title
import org.jetbrains.compose.resources.stringResource

private val CollectionScreenEntries = CollectionScreenType.entries

@Composable
fun CollectionScreen(
    state: CollectionState,
    coins: LazyPagingItems<Coin>,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (CollectionScreenAction) -> Unit,
) {
    Scaffold(
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
    state: CollectionState,
    coins: LazyPagingItems<Coin>,
    paddingValues: PaddingValues,
    onScreenAction: (action: CollectionScreenAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .cutoutAwarePadding(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        StatsToolbar(
            title = stringResource(Res.string.collection_total_value),
            totalValue = state.totalCollectionValue,
            objectCount = state.totalCoinCount,
            issuerCount = state.totalIssuerCount,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CollectionSegmentedButtons(
            selectedOption = state.selectedScreenType,
            onChangeScreenType = { onScreenAction(CollectionScreenAction.ChangeScreenType(it)) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = gridConfig.gridCells,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            stickyHeader {
                Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    TextButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.FilterAlt,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.collection_filter))
                    }

                    TextButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.FormatLineSpacing,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.collection_sort))
                    }

                    Spacer(Modifier.weight(1f))

                    FilledIconToggleButton(
                        checked = state.isCoinMultiSelectModeEnabled,
                        onCheckedChange = { },
                        colors = IconButtonDefaults.filledIconToggleButtonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Checklist,
                            contentDescription = null,
                            tint = if (state.isCoinMultiSelectModeEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (coins.loadState.refresh is LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            items(
                count = coins.itemCount,
                key = coins.itemKey { it.id }
            ) { index ->
                coins[index]?.let { item ->
                    CoinItem(
                        coin = item,
                        modifier = Modifier.width(320.dp).animateItem(),
                        onClick = { onScreenAction(CollectionScreenAction.NavigateToCoin(item.id)) }
                    )
                }
            }

            if (coins.loadState.refresh is LoadState.NotLoading && coins.itemSnapshotList.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyContent(
                        icon = Icons.AutoMirrored.Outlined.Feed,
                        title = Res.string.feed_empty_listing_title,
                        text = Res.string.feed_empty_listing_text,
                        modifier = Modifier
                    )
                }
            }

            if (coins.loadState.append is LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun CollectionSegmentedButtons(
    selectedOption: CollectionScreenType,
    onChangeScreenType: (type: CollectionScreenType) -> Unit
) {
    val entries = CollectionScreenEntries

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
        ) {
            entries.forEachIndexed { index, type ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = entries.size
                    ),
                    selected = selectedOption == type,
                    onClick = { onChangeScreenType(type) },
                    icon = { }
                ) {
                    Text(
                        text = stringResource(type.screenName),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CollectionScreenPreview() {
    val samplePagingData = PagingData.from(Coin.dummyItemsList(3))
    val flow = MutableStateFlow(samplePagingData)
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    AppTheme {
        CollectionScreen(
            state = CollectionState(),
            coins = lazyPagingItems,
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}