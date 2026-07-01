package identify.di

import identify.data.IdentifyRepositoryImpl
import identify.domain.IdentifyRepository
import identify.presentation.ui.identify.IdentifyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val identifyModule = module {
    single<IdentifyRepository> { IdentifyRepositoryImpl(get()) }
    viewModel { IdentifyViewModel(get(), get(), get()) }
}
