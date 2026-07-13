package collections.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import app.util.epochMilliToDateRange
import coil3.compose.AsyncImage
import collections.domain.model.CoinSet
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.coin_placeholder
import mintmind.shared.generated.resources.collection_last_updated
import mintmind.shared.generated.resources.collection_set_coin_count
import mintmind.shared.generated.resources.collection_set_empty
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

private const val MAX_PREVIEWS = 5
private val COIN_SIZE = 60.dp

@Composable
fun SetItem(
    set: CoinSet,
    modifier: Modifier = Modifier,
    onClick: (id: String) -> Unit,
) {
    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick(set.id) }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SetPreviews(previewUrls = set.previewObverseUrls)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = pluralStringResource(
                        Res.plurals.collection_set_coin_count,
                        set.coinCount,
                        set.coinCount
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = set.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${stringResource(Res.string.collection_last_updated)} ${set.createdAt.epochMilliToDateRange()}",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Light,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SetPreviews(
    previewUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    if (previewUrls.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(COIN_SIZE)
        ) {
            Text(
                text = stringResource(Res.string.collection_set_empty),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Light
            )
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(COIN_SIZE)
            // Offscreen compositing is required for BlendMode.Clear to punch
            // through only this layer instead of everything behind it.
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    ) {
        var offset = 0.dp
        previewUrls.take(MAX_PREVIEWS).forEach { url ->
            SetObject(
                strokeWidth = 10f,
                ringColor = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.offset(x = offset)
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.coin_placeholder),
                    error = painterResource(Res.drawable.coin_placeholder),
                    modifier = Modifier.fillMaxSize()
                )
            }
            offset += COIN_SIZE / 2
        }
    }
}

@Composable
private fun SetObject(
    strokeWidth: Float,
    ringColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(COIN_SIZE)
            .drawWithContent {
                drawContent()
                // Punch a ring-shaped hole so the coin beneath shows through the
                // gap, visually separating the overlapping discs.
                drawCircle(
                    color = ringColor,
                    radius = size.minDimension / 2,
                    center = size.center,
                    style = Stroke(width = strokeWidth),
                    blendMode = BlendMode.Clear
                )
            }
            .clip(CircleShape)
    ) {
        content()
    }
}

@Preview
@Composable
private fun SetItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            SetItem(
                set = CoinSet(
                    id = "1",
                    name = "Roman Empire",
                    description = "Ancient Roman coins",
                    previewObverseUrls = listOf("", "", "", "", ""),
                    coinCount = 12,
                    createdAt = 1756131199000L
                ),
                modifier = Modifier,
                onClick = { }
            )
        }
    }
}