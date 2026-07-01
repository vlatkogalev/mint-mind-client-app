package collections.domain.model

data class Coin(
    val id: String,
    val obverseUrl: String,
    val reverseUrl: String,
    val obverseThumbnailUrl: String?,
    val reverseThumbnailUrl: String?,
    val denomination: String,
    val countryOrIssuer: String,
    val year: Int?,
    val mintage: Long?,
    val gradeName: String,
    val gradeAbbreviation: String?,
    val gradeNumeric: Int?,
    val estimatedValueMean: Double,
    val setId: String?,
    val createdAt: Long
) {
    companion object {
        val dummyItem = Coin(
            id = "123",
            obverseUrl = "https://example.com/obverse.jpg",
            reverseUrl = "https://example.com/reverse.jpg",
            obverseThumbnailUrl = "https://en.numista.com/catalogue/photos/example-obverse-180.jpg",
            reverseThumbnailUrl = "https://en.numista.com/catalogue/photos/example-reverse-180.jpg",
            denomination = "Test Denomination",
            countryOrIssuer = "Macedonia",
            year = 2022,
            mintage = null,
            gradeName = "About Uncirculated",
            gradeAbbreviation = "AU",
            gradeNumeric = 58,
            estimatedValueMean = 1.00,
            setId = null,
            createdAt = 1672531200L
        )

        fun dummyItemsList(count: Int = 5): List<Coin> {
            return (1..count).map {
                dummyItem.copy(id = it.toString())
            }
        }
    }
}