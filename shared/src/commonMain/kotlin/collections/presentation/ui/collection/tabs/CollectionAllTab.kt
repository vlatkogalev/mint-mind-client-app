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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
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
import collections.presentation.ui.collection.CollectionState
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_filter
import mintmind.shared.generated.resources.collection_sort
import mintmind.shared.generated.resources.feed_empty_listing_text
import mintmind.shared.generated.resources.feed_empty_listing_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionAllTab(
    state: CollectionState,
    coins: LazyPagingItems<Coin>,
    onIdentifyCoin: () -> Unit,
    onSelectCoin: (id: String) -> Unit,
    onCheckCoin: (coin: Coin) -> Unit,
    onClickFilter: () -> Unit,
    onClickSort: () -> Unit,
    onDeleteSelectedCoins: () -> Unit,
    onMoveSelectedCoins: () -> Unit,
    onCoinMultiSelectionModeEnabled: () -> Unit
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

        items(count = coins.itemCount, key = coins.itemKey { it.id }) { index ->
            val coin = coins[index]
            if (coin != null) {
                CoinItem(
                    coin = coin,
                    modifier = Modifier.width(320.dp).animateItem(),
                    onClick = { onSelectCoin(coin.id) }
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