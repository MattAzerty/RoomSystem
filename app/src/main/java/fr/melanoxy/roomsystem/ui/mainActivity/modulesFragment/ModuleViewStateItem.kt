package fr.melanoxy.roomsystem.ui.mainActivity.modulesFragment
//TODO: how to have one adapter but several "stateItem"
data class ModuleViewStateItem (
    val moduleId: Int,
    val moduleName: String,
    val moduleImageUrl: String,
    val onModuleClicked: () -> Unit,
)