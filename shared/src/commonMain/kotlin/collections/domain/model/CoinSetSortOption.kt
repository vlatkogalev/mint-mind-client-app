package collections.domain.model

import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_sort_coin_count_high_to_low
import mintmind.shared.generated.resources.collection_sort_coin_count_low_to_high
import mintmind.shared.generated.resources.collection_sort_date_created_new_to_old
import mintmind.shared.generated.resources.collection_sort_date_created_old_to_new
import mintmind.shared.generated.resources.collection_sort_date_updated_new_to_old
import mintmind.shared.generated.resources.collection_sort_date_updated_old_to_new
import mintmind.shared.generated.resources.collection_sort_total_value_high_to_low
import mintmind.shared.generated.resources.collection_sort_total_value_low_to_high
import org.jetbrains.compose.resources.StringResource

enum class CoinSetSortOption(
    val wireValue: String,
    val label: StringResource,
) {
    DATE_CREATED_NEW_TO_OLD(
        wireValue = "DATE_CREATED_NEW_TO_OLD",
        label = Res.string.collection_sort_date_created_new_to_old,
    ),
    DATE_CREATED_OLD_TO_NEW(
        wireValue = "DATE_CREATED_OLD_TO_NEW",
        label = Res.string.collection_sort_date_created_old_to_new,
    ),
    DATE_UPDATED_NEW_TO_OLD(
        wireValue = "DATE_UPDATED_NEW_TO_OLD",
        label = Res.string.collection_sort_date_updated_new_to_old,
    ),
    DATE_UPDATED_OLD_TO_NEW(
        wireValue = "DATE_UPDATED_OLD_TO_NEW",
        label = Res.string.collection_sort_date_updated_old_to_new,
    ),
    COIN_COUNT_HIGH_TO_LOW(
        wireValue = "COIN_COUNT_HIGH_TO_LOW",
        label = Res.string.collection_sort_coin_count_high_to_low,
    ),
    COIN_COUNT_LOW_TO_HIGH(
        wireValue = "COIN_COUNT_LOW_TO_HIGH",
        label = Res.string.collection_sort_coin_count_low_to_high,
    ),
    TOTAL_VALUE_HIGH_TO_LOW(
        wireValue = "TOTAL_VALUE_HIGH_TO_LOW",
        label = Res.string.collection_sort_total_value_high_to_low,
    ),
    TOTAL_VALUE_LOW_TO_HIGH(
        wireValue = "TOTAL_VALUE_LOW_TO_HIGH",
        label = Res.string.collection_sort_total_value_low_to_high,
    );

    companion object {
        val DEFAULT = DATE_CREATED_NEW_TO_OLD
    }
}
