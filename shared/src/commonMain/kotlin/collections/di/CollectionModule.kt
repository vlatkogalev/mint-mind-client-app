package collections.di

import app.util.coreComponent
import collections.data.CollectionRepositoryImpl
import collections.domain.CollectionRepository
import collections.presentation.ui.coin.CoinViewModel
import collections.presentation.ui.collection.CollectionViewModel
import collections.presentation.ui.set.SetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val collectionModule = module {
    single<CoroutineScope>(named("appScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    single<CollectionRepository> {
        CollectionRepositoryImpl(
            get(),
            get(),
            coreComponent.tokenManager,
            get(qualifier = named("appScope"))
        )
    }

    viewModel { CollectionViewModel(get()) }
    viewModel { (id: String) -> CoinViewModel(id, get()) }
    viewModel { (id: String) -> SetViewModel(id, get()) }
}