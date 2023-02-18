package fr.melanoxy.roomsystem.ui.modules

data class ModuleViewStateItem (
    val moduleId: Int,
    val moduleName: String,
    val moduleImageUrl: String,
    val onModuleClicked: () -> Unit,
)