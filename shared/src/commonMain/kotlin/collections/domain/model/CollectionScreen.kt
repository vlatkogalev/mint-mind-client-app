package collections.domain.model

import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.collection_screen_all
import mintmind.shared.generated.resources.collection_screen_sets
import mintmind.shared.generated.resources.collection_screen_summary
import org.jetbrains.compose.resources.StringResource

enum class CollectionScreenType(val screenName: StringResource) {
    SUMMARY(screenName = Res.string.collection_screen_summary),
    ALL(screenName = Res.string.collection_screen_all),
    SETS(screenName = Res.string.collection_screen_sets);
}