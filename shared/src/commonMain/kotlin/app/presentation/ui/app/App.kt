package app.presentation.ui.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import app.navigation.graph.AppGraph
import app.presentation.theme.AppTheme
import app.util.ChangeSystemUIColors
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_no_internet
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel = koinViewModel<AppViewModel>()

    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.storeData()
        }
    }

    setSingletonImageLoaderFactory {
        ImageLoader.Builder(it)
            .components { addPlatformFileSupport() }
            .crossfade(true)
            .build()
    }

    ChangeSystemUIColors(true)

    AppTheme {
        Column {
            ConnectionBanner(state.isDisconnected)
            AppGraph()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ConnectionBanner(
    isDisconnected: Boolean,
    modifier: Modifier = Modifier
) {
    val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    AnimatedVisibility(
        visible = isDisconnected,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Text(
            text = stringResource(Res.string.error_no_internet),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(top = topPadding, start = 8.dp, end = 8.dp, bottom = 8.dp)
        )
    }
}