package app.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.util.getPlatform
import collections.data.local.converter.Converters
import collections.data.local.dao.CoinDao
import collections.data.local.dao.CoinDetailsDao
import collections.data.local.dao.CoinPagingStateDao
import collections.data.local.dao.CoinSetDao
import collections.data.local.dao.CollectionStatsDao
import collections.data.local.entity.AiAnalysisEntity
import collections.data.local.entity.CatalogueNumberEntity
import collections.data.local.entity.CoinDataEntity
import collections.data.local.entity.CoinDetailsEntity
import collections.data.local.entity.CoinEntity
import collections.data.local.entity.CoinPagingStateEntity
import collections.data.local.entity.CoinSetEntity
import collections.data.local.entity.CollectionHighlightsEntity
import collections.data.local.entity.CollectionStatsEntity
import feed.data.local.dao.CoinListingDao
import feed.data.local.dao.PostDao
import feed.data.local.entity.CoinListingEntity
import feed.data.local.entity.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import user.data.local.dao.UserDao
import user.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        CoinListingEntity::class,
        CoinDetailsEntity::class,
        CoinDataEntity::class,
        AiAnalysisEntity::class,
        CatalogueNumberEntity::class,
        CollectionStatsEntity::class,
        CollectionHighlightsEntity::class,
        CoinSetEntity::class,
        CoinEntity::class,
        CoinPagingStateEntity::class,
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun coinListingDao(): CoinListingDao
    abstract fun coinDetailsDao(): CoinDetailsDao
    abstract fun collectionStatsDao(): CollectionStatsDao
    abstract fun coinSetDao(): CoinSetDao
    abstract fun coinDao(): CoinDao
    abstract fun coinPagingStateDao(): CoinPagingStateDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getAppDatabase(): AppDatabase {
    return getPlatform().getDatabaseBuilder()
        // TODO: replace destructive migration before shipping offline WRITES
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}