package app.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

actual val platformModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
}