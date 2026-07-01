package feed.di

import feed.data.FeedRepositoryImpl
import feed.domain.FeedRepository
import feed.presentation.ui.coin_listings.CoinListingsViewModel
import feed.presentation.ui.news_feed.NewsFeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val feedModule = module {
    single<FeedRepository> { FeedRepositoryImpl(get(named("AuthClient")), get()) }

    viewModel { NewsFeedViewModel(get()) }
    viewModel { CoinListingsViewModel(get()) }
}