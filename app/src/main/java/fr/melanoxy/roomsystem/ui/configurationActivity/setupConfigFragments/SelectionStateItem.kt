package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

data class SelectionStateItem (
    val selectionId: Int,
    val selectionField: String,
    val onSelectionClicked: () -> Unit,
)