package identify.presentation.ui.identify

sealed interface IdentifyFlowEvent {
    data object NavigateToResult : IdentifyFlowEvent
    data object NavigateToIdentifyFlow : IdentifyFlowEvent
    data class ShowMessage(val message: String) : IdentifyFlowEvent
}
