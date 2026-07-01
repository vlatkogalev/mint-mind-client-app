package collections.di

import collections.data.CollectionRepositoryImpl
import collections.domain.CollectionRepository
import collections.presentation.ui.coin.CoinViewModel
import collections.presentation.ui.collection.CollectionViewModel
import collections.presentation.ui.set.SetViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val collectionModule = module {
    single<CollectionRepository> { CollectionRepositoryImpl(get(), get()) }

    viewModel { CollectionViewModel(get()) }
    viewModel { (id: String) -> CoinViewModel(id, get()) }
    viewModel { (id: String) -> SetViewModel(id, get()) }
}