package app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    title: String?,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { TopAppBarText(text = title ?: "") },
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.tertiary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.tertiary,
            subtitleContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MainTopAppBarPreview() {
    AppTheme {
        MainTopAppBar(
            title = "Home",
            onNavigateUp = {}
        )
    }
}