package collections.presentation.ui.collection.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.presentation.components.ReededDivider
import app.presentation.components.vector.LaurelLeft
import app.presentation.components.vector.LaurelRight
import app.presentation.theme.AppThemeExt
import app.presentation.util.calculateGridConfig
import coil3.compose.AsyncImage
import collections.domain.model.CollectionStats
import collections.presentation.ui.collection.CollectionScreenAction
import collections.presentation.ui.collection.CollectionState
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_most_ancient
import mintmind.shared.generated.resources.collection_most_valuable
import mintmind.shared.generated.resources.collection_rarest
import mintmind.shared.generated.resources.identify_coin_side_obverse
import mintmind.shared.generated.resources.identify_coin_side_reverse
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionSummaryTab(
    state: CollectionState,
    onAction: (CollectionScreenAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val gridConfig = windowSizeClass.calculateGridConfig(1, 2, 3)

    LazyVerticalGrid(
        columns = gridConfig.gridCells,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        highlightItems(
            highlights = state.highlights,
            showDivider = gridConfig.columnCount <= 2,
            onAction = onAction,
        )
    }
}

private fun LazyGridScope.highlightItems(
    highlights: List<CollectionStats.HighlightedCoin>?,
    showDivider: Boolean,
    onAction: (CollectionScreenAction) -> Unit,
) {
    highlights ?: return

    items(
        count = highlights.size,
        key = { index -> highlights[index].type }
    ) { index ->
        val coin = highlights[index]

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clickable(onClick = {
                onAction(
                    CollectionScreenAction.NavigateToCoin(
                        coin.coinId
                    )
                )
            })
        ) {
            BestObjectItem(
                name = coin.denomination,
                countryOrIssuer = coin.countryOrIssuer,
                obverseImagePath = coin.obverseUrl,
                reverseImagePath = coin.reverseUrl,
                value = coin.highlightValue(),
                label = stringResource(coin.type.labelRes())
            )

            if (showDivider) {
                ReededDivider()
            }
        }
    }
}

private fun CollectionStats.HighlightedCoin.highlightValue(): String? =
    when (type) {
        CollectionStats.HighlightType.MOST_VALUABLE -> "$$estimatedValueMean"
        CollectionStats.HighlightType.MOST_ANCIENT -> year?.toString()
        CollectionStats.HighlightType.RAREST -> mintage.toString()
    }

private fun CollectionStats.HighlightType.labelRes(): StringResource =
    when (this) {
        CollectionStats.HighlightType.MOST_VALUABLE -> Res.string.collection_most_valuable
        CollectionStats.HighlightType.MOST_ANCIENT -> Res.string.collection_most_ancient
        CollectionStats.HighlightType.RAREST -> Res.string.collection_rarest
    }

@Composable
private fun BestObjectItem(
    name: String?,
    countryOrIssuer: String?,
    obverseImagePath: String?,
    reverseImagePath: String?,
    value: String?,
    label: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = name ?: "",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = countryOrIssuer ?: "",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = AppThemeExt.colors.textSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = obverseImagePath,
                contentDescription = stringResource(Res.string.identify_coin_side_obverse),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            AsyncImage(
                model = reverseImagePath,
                contentDescription = stringResource(Res.string.identify_coin_side_reverse),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = LaurelLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    color = AppThemeExt.colors.textSecondary,
                )
            }

            Icon(
                imageVector = LaurelRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}