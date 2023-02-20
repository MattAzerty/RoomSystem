package fr.melanoxy.roomsystem.ui.configurationActivity.configurationFragment

data class SelectionStateItem (
    val selectionId: Int,
    val selectionField: String,
    val onSelectionClicked: () -> Unit,
)