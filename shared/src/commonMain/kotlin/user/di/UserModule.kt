package user.di

import app.util.coreComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import user.data.UserRepositoryImpl
import user.domain.UserRepository
import user.domain.usecase.LogoutUseCase
import user.domain.usecase.ValidateConfirmEmailUseCase
import user.domain.usecase.ValidateConfirmPasswordUseCase
import user.domain.usecase.ValidateEmailUseCase
import user.domain.usecase.ValidatePasswordUseCase
import user.presentation.ui.user.UserViewModel

val userModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    factoryOf(::ValidateEmailUseCase)
    factoryOf(::ValidateConfirmEmailUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::ValidateConfirmPasswordUseCase)
    factory { LogoutUseCase(get(), get(), get(), coreComponent.tokenManager) }

    viewModel { UserViewModel(get(), get()) }
}
