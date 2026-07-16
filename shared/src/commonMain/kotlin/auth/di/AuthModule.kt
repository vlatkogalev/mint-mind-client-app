package auth.di

import auth.data.AuthRepositoryImpl
import auth.domain.AuthRepository
import auth.presentation.ui.login.LoginViewModel
import auth.presentation.ui.register.RegisterViewModel
import auth.presentation.ui.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            get(qualifier = named("AuthClient")),
            get()
        )
    }

    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel {
        RegisterViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { ResetPasswordViewModel(get(), get()) }
}