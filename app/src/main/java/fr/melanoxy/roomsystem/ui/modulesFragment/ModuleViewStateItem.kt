package fr.melanoxy.roomsystem.ui.modulesFragment

data class ModuleViewStateItem (
    val moduleId: Int,
    val moduleName: String,
    val moduleImageUrl: String,
    val onModuleClicked: () -> Unit,
)