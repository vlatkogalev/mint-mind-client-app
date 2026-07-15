package collections.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class SortOptionWireValueTest {

    @Test
    fun coinSortOptionWireValuesMatchEnumNames() {
        CoinSortOption.entries.forEach { option ->
            assertEquals(option.name, option.wireValue)
        }
    }

    @Test
    fun coinSetSortOptionWireValuesMatchEnumNames() {
        CoinSetSortOption.entries.forEach { option ->
            assertEquals(option.name, option.wireValue)
        }
    }
}
