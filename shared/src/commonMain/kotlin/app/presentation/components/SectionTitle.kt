package app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.presentation.theme.AppTheme
import app.presentation.theme.AppThemeExt
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.load_more
import org.jetbrains.compose.resources.stringResource

@Composable
fun SectionTitle(
    title: String,
    actionText: String? = null,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(1f)
        )

        if (actionText != null) {
            TextButton(
                contentPadding = PaddingValues(horizontal = 0.dp),
                onClick = onLoadMore,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppThemeExt.colors.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun SectionTitlePreview() {
    AppTheme {
        SectionTitle(
            title = "Title",
            actionText = stringResource(Res.string.load_more)
        )
    }
}

