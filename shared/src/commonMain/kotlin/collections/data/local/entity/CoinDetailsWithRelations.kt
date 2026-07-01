package collections.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CoinDetailsWithRelations(
    @Embedded
    val coin: CoinDetailsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "coinId"
    )
    val coinData: CoinDataEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "coinId"
    )
    val aiAnalysis: AiAnalysisEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "coinId"
    )
    val catalogueNumbers: List<CatalogueNumberEntity>
)
