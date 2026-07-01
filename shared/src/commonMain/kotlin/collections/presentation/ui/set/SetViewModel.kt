package collections.presentation.ui.set

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SetViewModel(
    private val setId: String,
    private val collectionRepository: collections.domain.CollectionRepository,
) : ViewModel() {
    private val _state =
        MutableStateFlow(SetState())
    val state: StateFlow<SetState> = _state

    init {

    }
}