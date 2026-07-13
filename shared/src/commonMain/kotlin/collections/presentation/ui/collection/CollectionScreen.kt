package collections.presentation.ui.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.presentation.theme.AppTheme
import app.presentation.util.cutoutAwarePadding
import collections.domain.model.Coin
import collections.domain.model.CollectionScreenType
import collections.presentation.components.StatsToolbar
import collections.presentation.ui.collection.tabs.CollectionAllTab
import collections.presentation.ui.collection.tabs.CollectionSetsTab
import collections.presentation.ui.collection.tabs.CollectionSummaryTab
import kotlinx.coroutines.flow.MutableStateFlow
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_total_value
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
    val pagerState = rememberPagerState(
        pageCount = { CollectionScreenEntries.size },
        initialPage = state.selectedScreenType.ordinal
    )

    LaunchedEffect(state.selectedScreenType) {
        pagerState.animateScrollToPage(state.selectedScreenType.ordinal)
    }

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

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (CollectionScreenEntries[page]) {
                CollectionScreenType.SUMMARY -> {
                    CollectionSummaryTab(
                        state = state,
                        onIdentifyCoin = { },
                        onSelectCoin = { onScreenAction(CollectionScreenAction.NavigateToCoin(it)) },
                    )
                }

                CollectionScreenType.ALL -> {
                    CollectionAllTab(
                        state = state,
                        coins = coins,
                        onIdentifyCoin = { },
                        onSelectCoin = { onScreenAction(CollectionScreenAction.NavigateToCoin(it)) },
                        onCheckCoin = { },
                        onClickFilter = { },
                        onClickSort = { },
                        onDeleteSelectedCoins = { },
                        onMoveSelectedCoins = { },
                        onCoinMultiSelectionModeEnabled = { },
                    )
                }

                CollectionScreenType.SETS -> {
                    CollectionSetsTab(
                        state = state,
                        onClickCreateNewSet = { },
                        onSelectSet = { },
                        onCheckSet = { },
                        onClickSort = { },
                        onDeleteSelectedSets = { },
                        onSetMultiSelectionModeEnabled = { },
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
    val samplePagingData = PagingData.from(Coin.dummyItemsList(0))
    val flow = MutableStateFlow(samplePagingData)
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    AppTheme {
        CollectionScreen(
            state = CollectionState(
                selectedScreenType = CollectionScreenType.ALL
            ),
            coins = lazyPagingItems,
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = {}
        )
    }
}