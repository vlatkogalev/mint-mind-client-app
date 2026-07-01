package app.di

import app.data.remote.createBaseHttpClient
import app.data.remote.createS3HttpClient
import app.data.remote.createSSEHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    single<HttpClient>(qualifier = named("AuthClient")) {
        createBaseHttpClient(
            json = get(),
            enableAuth = false
        )
    }

    single<HttpClient> {
        createBaseHttpClient(
            json = get(),
            enableAuth = true,
            authClient = get(qualifier = named("AuthClient"))
        )
    }

    single<HttpClient>(qualifier = named("SSEClient")) {
        createSSEHttpClient()
    }

    single<HttpClient>(qualifier = named("S3Client")) {
        createS3HttpClient()
    }
}