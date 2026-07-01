package feed.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import app.util.epochMilliToDateRange
import coil3.compose.AsyncImage
import feed.domain.model.Post
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun Post(
    post: Post,
    modifier: Modifier,
    onClickPost: (id: String) -> Unit,
) {
    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        )
    ) {
        Column(
            modifier = modifier
                .clickable { onClickPost(post.url) }
                .padding(16.dp)
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = null,
                placeholder = painterResource(Res.drawable.image_placeholder),
                error = painterResource(Res.drawable.image_placeholder),
                contentScale = ContentScale.FillHeight,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 2f / 1f)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                modifier = modifier.fillMaxWidth()
            )
            Spacer(modifier = modifier.height(8.dp))
            PostFooterItem(
                icon = Icons.Outlined.AccessTime,
                text = post.publishedAt.epochMilliToDateRange(),
                modifier = modifier.height(16.dp).fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PostFooterItem(
    icon: ImageVector?,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier,
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(
                    align = Alignment.CenterVertically
                )
        )
    }
}

@Preview
@Composable
private fun PostPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Post(
                post = Post.dummyItem,
                modifier = Modifier,
                onClickPost = { }
            )
        }
    }
}