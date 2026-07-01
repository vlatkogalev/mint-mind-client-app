package collections.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import collections.data.local.entity.AiAnalysisEntity
import collections.data.local.entity.CatalogueNumberEntity
import collections.data.local.entity.CoinDataEntity
import collections.data.local.entity.CoinDetailsEntity
import collections.data.local.entity.CoinDetailsWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDetailsDao {

    @Transaction
    @Query("SELECT * FROM coin_details WHERE id = :id")
    fun getByIdWithRelations(id: String): Flow<CoinDetailsWithRelations?>

    @Query("SELECT * FROM coin_details ORDER BY createdAt DESC")
    fun getAll(): PagingSource<Int, CoinDetailsEntity>

    @Upsert
    suspend fun upsert(coin: CoinDetailsEntity)

    @Upsert
    suspend fun upsertAll(coins: List<CoinDetailsEntity>)

    @Upsert
    suspend fun upsertCoinData(coinData: CoinDataEntity)

    @Upsert
    suspend fun upsertAiAnalysis(aiAnalysis: AiAnalysisEntity)

    @Upsert
    suspend fun upsertCatalogueNumbers(numbers: List<CatalogueNumberEntity>)

    @Query("DELETE FROM coin_data WHERE coinId = :coinId")
    suspend fun deleteCoinData(coinId: String)

    @Query("DELETE FROM ai_analysis WHERE coinId = :coinId")
    suspend fun deleteAiAnalysis(coinId: String)

    @Query("DELETE FROM catalogue_numbers WHERE coinId = :coinId")
    suspend fun deleteCatalogueNumbers(coinId: String)

    @Transaction
    suspend fun upsertCoinDetails(
        coin: CoinDetailsEntity,
        coinData: CoinDataEntity,
        aiAnalysis: AiAnalysisEntity,
        catalogueNumbers: List<CatalogueNumberEntity>
    ) {
        upsert(coin)
        deleteCoinData(coin.id)
        upsertCoinData(coinData)
        deleteAiAnalysis(coin.id)
        upsertAiAnalysis(aiAnalysis)
        deleteCatalogueNumbers(coin.id)
        if (catalogueNumbers.isNotEmpty()) {
            upsertCatalogueNumbers(catalogueNumbers)
        }
    }

    @Query("DELETE FROM coin_details WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM coin_details")
    suspend fun deleteAll()
}
