package identify.di

import identify.presentation.ui.identify.IdentifyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val identifyModule = module {
    viewModel { IdentifyViewModel(get(), get(), get()) }
}
