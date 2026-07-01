package app.di

import app.data.local.AppPreferences
import app.data.local.AppPreferencesImpl
import app.data.local.getAppDatabase
import app.domain.usecase.ValidateCommonFieldUseCase
import app.domain.usecase.ValidateCommonNumericFieldUseCase
import app.domain.usecase.ValidateDateFieldUseCase
import app.domain.usecase.ValidateFieldInRangeUseCase
import app.presentation.ui.app.AppViewModel
import app.presentation.ui.home.HomeViewModel
import app.presentation.ui.onboarding.OnboardingViewModel
import dev.jordond.connectivity.Connectivity
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppPreferences> { AppPreferencesImpl(get()) }
    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
    single { getAppDatabase() }
    single { Connectivity() }

    viewModel { AppViewModel(get(), get(), get()) }
    viewModel { OnboardingViewModel() }
    viewModel { HomeViewModel(get()) }

    factoryOf(::ValidateCommonFieldUseCase)
    factoryOf(::ValidateCommonNumericFieldUseCase)
    factoryOf(::ValidateDateFieldUseCase)
    factoryOf(::ValidateFieldInRangeUseCase)
}