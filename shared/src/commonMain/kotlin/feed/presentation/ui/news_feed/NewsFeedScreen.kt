package feed.presentation.ui.news_feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import app.presentation.components.EmptyContent
import app.presentation.components.RedirectDialog
import app.presentation.theme.AppTheme
import app.presentation.util.calculateGridConfig
import app.presentation.util.cutoutAwarePaddingValues
import feed.domain.model.Post
import feed.presentation.components.Post
import kotlinx.coroutines.flow.MutableStateFlow
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.feed_empty_listing_text
import mintmind.shared.generated.resources.feed_empty_listing_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen(
    state: NewsFeedState,
    posts: LazyPagingItems<Post>,
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (NewsFeedScreenAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val statusBars = WindowInsets.statusBars.asPaddingValues()
    val systemBottomPadding = statusBars.calculateBottomPadding()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Coin Talk", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onScreenAction(NewsFeedScreenAction.NavigateUp) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                    subtitleContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                ),
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        MainContent(
            posts = posts,
            paddingValues = paddingValues,
            systemBottomPadding = systemBottomPadding,
            windowSizeClass = windowSizeClass,
            onClickPost = { url -> onScreenAction(NewsFeedScreenAction.LoadPost(url)) }
        )

        if (state.showRedirectDialog) {
            state.redirectUri?.let { uri ->
                RedirectDialog(
                    uri = uri,
                    modifier = Modifier,
                    onDismissRequest = {
                        onScreenAction(
                            NewsFeedScreenAction.ToggleRedirectDialog(
                                null
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    posts: LazyPagingItems<Post>,
    paddingValues: PaddingValues,
    systemBottomPadding: Dp,
    windowSizeClass: WindowSizeClass,
    onClickPost: (String?) -> Unit
) {
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    LazyVerticalGrid(
        columns = gridConfig.gridCells,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = cutoutAwarePaddingValues(
            top = paddingValues.calculateTopPadding() + 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = systemBottomPadding + 96.dp
        )
    ) {
        if (posts.loadState.refresh is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        items(
            count = posts.itemCount,
            key = posts.itemKey { it.id }
        ) { index ->
            posts[index]?.let { item ->
                Post(
                    post = item,
                    modifier = Modifier.animateItem(),
                    onClickPost = { onClickPost(it) }
                )
            }
        }

        if (posts.loadState.refresh is LoadState.NotLoading && posts.itemSnapshotList.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EmptyContent(
                    icon = Icons.AutoMirrored.Outlined.Feed,
                    title = Res.string.feed_empty_listing_title,
                    text = Res.string.feed_empty_listing_text,
                    modifier = Modifier
                )
            }
        }

        if (posts.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewsFeedScreenPreview() {
    AppTheme {
        val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
        val samplePagingData = PagingData.from(Post.dummyItemsList(3))
        val flow = MutableStateFlow(samplePagingData)
        val lazyPagingItems = flow.collectAsLazyPagingItems()

        NewsFeedScreen(
            state = NewsFeedState(),
            posts = lazyPagingItems,
            windowSizeClass = windowSizeClass,
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = { }
        )
    }
}