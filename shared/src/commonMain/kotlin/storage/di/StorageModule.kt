package storage.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import storage.data.StorageRepositoryImpl
import storage.domain.StorageRepository

val storageModule = module {
    single<StorageRepository> {
        StorageRepositoryImpl(
            httpClient = get(),
            s3HttpClient = get(qualifier = named("S3Client"))
        )
    }
}
