package app.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.components.AppTopBar
import app.presentation.components.PrimaryButton
import app.presentation.components.RedirectDialog
import app.presentation.components.ReededDivider
import app.presentation.components.SectionTitle
import app.presentation.components.TopAppBarText
import app.presentation.theme.AppTheme
import app.presentation.theme.AppThemeExt
import app.presentation.util.calculateGridConfig
import app.presentation.util.cutoutAwarePaddingValues
import feed.domain.model.CoinListing
import feed.domain.model.Post
import feed.presentation.components.CoinListingItem
import feed.presentation.components.Post
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.app_name
import mintmind.shared.generated.resources.home_screen_identify_button_text
import mintmind.shared.generated.resources.home_screen_identify_card_subtitle
import mintmind.shared.generated.resources.home_screen_identify_card_title
import mintmind.shared.generated.resources.image_rare_coin
import mintmind.shared.generated.resources.load_more
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onScreenAction: (action: HomeScreenAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            AppTopBar(
                title = { TopAppBarText(text = stringResource(Res.string.app_name)) },
                actions = {
                    IconButton(onClick = { onScreenAction(HomeScreenAction.NavigateToUser) }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = null
                        )
                    }
                },
                transparent = true,
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        MainContent(
            state = state,
            paddingValues = paddingValues,
            onScreenAction = onScreenAction
        )
    }

    if (state.showRedirectDialog) {
        state.redirectUri?.let { uri ->
            RedirectDialog(
                uri = uri,
                modifier = Modifier,
                onDismissRequest = {
                    onScreenAction(HomeScreenAction.ToggleRedirectDialog(null))
                }
            )
        }
    }
}

@Composable
private fun MainContent(
    state: HomeState,
    paddingValues: PaddingValues,
    onScreenAction: (action: HomeScreenAction) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = cutoutAwarePaddingValues(
            top = paddingValues.calculateTopPadding() + 16.dp,
            applyStartCutoutPadding = false
        )
    ) {
        item {
            HomeObject(
                onIdentify = { onScreenAction(HomeScreenAction.NavigateToIdentify) },
                modifier = Modifier.padding(horizontal = 24.dp).animateItem()
            )
        }

        if (state.coinListings.isNotEmpty()) {
            item {
                CoinListings(
                    coinListings = state.coinListings,
                    onLoadMore = { onScreenAction(HomeScreenAction.NavigateToCoinListings) },
                    onClickListing = { onScreenAction(HomeScreenAction.ToggleRedirectDialog(it)) }
                )
            }
        }

        if (state.posts.isNotEmpty()) {
            item {
                NewsFeed(
                    posts = state.posts,
                    onLoadMore = { onScreenAction(HomeScreenAction.NavigateToNewsFeed) },
                    onClickPost = { onScreenAction(HomeScreenAction.ToggleRedirectDialog(it)) }
                )
            }
        }
    }
}

@Composable
private fun HomeObject(
    onIdentify: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                val radius = 550f

                val brush = Brush.radialGradient(
                    colors = listOf(surfaceColor, backgroundColor),
                    center = size.center,
                    radius = radius
                )

                onDrawBehind {
                    drawCircle(
                        brush = brush,
                        center = size.center,
                        radius = radius
                    )
                }
            }
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Image(
                bitmap = imageResource(Res.drawable.image_rare_coin),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.home_screen_identify_card_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(Res.string.home_screen_identify_card_subtitle),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = AppThemeExt.colors.textSecondary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                isLoading = false,
                text = stringResource(Res.string.home_screen_identify_button_text),
                onButtonClick = { onIdentify() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CoinListings(
    coinListings: List<CoinListing>,
    onClickListing: (url: String?) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(listState)

    Column {
        ReededDivider(Modifier.padding(horizontal = 16.dp))

        SectionTitle(
            title = "Ebay Listings",
            actionText = stringResource(Res.string.load_more),
            modifier = Modifier.padding(start = 24.dp, end = 16.dp),
            onLoadMore = onLoadMore
        )

        LazyRow(
            state = listState,
            flingBehavior = snapBehavior,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(coinListings) { item ->
                CoinListingItem(
                    coinListing = item,
                    modifier = Modifier.width(320.dp).animateItem(),
                    onClickPost = { url -> onClickListing(url) }
                )
            }
        }
    }
}

@Composable
private fun NewsFeed(
    posts: List<Post>,
    onClickPost: (url: String?) -> Unit,
    onLoadMore: () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    Column {
        ReededDivider(Modifier.padding(horizontal = 16.dp))

        SectionTitle(
            title = "Coin Talk",
            actionText = stringResource(Res.string.load_more),
            modifier = Modifier.padding(start = 24.dp, end = 16.dp),
            onLoadMore = onLoadMore
        )

        LazyVerticalGrid(
            columns = gridConfig.gridCells,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            modifier = Modifier
                .heightIn(max = 2500.dp) // Important for nesting lazy grid in lazy column
                .fillMaxWidth()
        ) {
            items(items = posts, key = { item -> item.id }) { item ->
                Post(
                    post = item,
                    modifier = Modifier.animateItem(),
                    onClickPost = { url -> onClickPost(url) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            state = HomeState(
                posts = Post.dummyItemsList(),
                coinListings = CoinListing.dummyItemsList()
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onScreenAction = { }
        )
    }
}