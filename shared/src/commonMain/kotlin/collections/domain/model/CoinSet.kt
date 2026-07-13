package collections.domain.model

data class CoinSet(
    val id: String,
    val name: String,
    val description: String?,
    val previewObverseUrls: List<String>,
    val coinCount: Int,
    val createdAt: Long
)