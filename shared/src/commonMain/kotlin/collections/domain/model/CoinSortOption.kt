package collections.domain.model

import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_sort_date_new_to_old
import mintmind.shared.generated.resources.collection_sort_date_old_to_new
import mintmind.shared.generated.resources.collection_sort_release_new_to_old
import mintmind.shared.generated.resources.collection_sort_release_old_to_new
import mintmind.shared.generated.resources.collection_sort_value_high_to_low
import mintmind.shared.generated.resources.collection_sort_value_low_to_high
import org.jetbrains.compose.resources.StringResource

enum class CoinSortOption(
    val wireValue: String,
    val label: StringResource,
) {
    DATE_ADDED_NEW_TO_OLD(
        wireValue = "DATE_ADDED_NEW_TO_OLD",
        label = Res.string.collection_sort_date_new_to_old,
    ),
    DATE_ADDED_OLD_TO_NEW(
        wireValue = "DATE_ADDED_OLD_TO_NEW",
        label = Res.string.collection_sort_date_old_to_new,
    ),
    VALUE_HIGH_TO_LOW(
        wireValue = "VALUE_HIGH_TO_LOW",
        label = Res.string.collection_sort_value_high_to_low,
    ),
    VALUE_LOW_TO_HIGH(
        wireValue = "VALUE_LOW_TO_HIGH",
        label = Res.string.collection_sort_value_low_to_high,
    ),
    RELEASE_YEAR_NEW_TO_OLD(
        wireValue = "RELEASE_YEAR_NEW_TO_OLD",
        label = Res.string.collection_sort_release_new_to_old,
    ),
    RELEASE_YEAR_OLD_TO_NEW(
        wireValue = "RELEASE_YEAR_OLD_TO_NEW",
        label = Res.string.collection_sort_release_old_to_new,
    );

    companion object {
        val DEFAULT = DATE_ADDED_NEW_TO_OLD
    }
}
