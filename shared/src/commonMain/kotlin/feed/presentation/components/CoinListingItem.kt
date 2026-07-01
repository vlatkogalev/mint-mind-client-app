package feed.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import coil3.compose.AsyncImage
import feed.domain.model.CoinListing
import feed.domain.util.getCurrencySymbol
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun CoinListingItem(
    coinListing: CoinListing,
    modifier: Modifier,
    onClickPost: (url: String?) -> Unit
) {
    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier.height(120.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClickPost(coinListing.listingUrl)
                }
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(6.dp))
            ) {
                Image(
                    painter = painterResource(Res.drawable.image_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                AsyncImage(
                    model = coinListing.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight().weight(1f)
            ) {
                Text(
                    text = coinListing.title,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    coinListing.condition?.let { condition ->
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                            SuggestionChip(
                                onClick = {},
                                label = { Text(text = condition) },
                                modifier = Modifier.padding(0.dp)
                            )
                        }
                    }
                    Text(
                        text = "${getCurrencySymbol(coinListing.currency)} ${coinListing.price}",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CoinListingItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            CoinListingItem(
                coinListing = CoinListing.dummyItem,
                modifier = Modifier,
                onClickPost = { }
            )
        }
    }
}