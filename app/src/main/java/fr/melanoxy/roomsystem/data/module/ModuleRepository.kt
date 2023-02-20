package fr.melanoxy.roomsystem.data.module

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepository @Inject constructor() {

    val modulesList = listOf(
        Module(
            moduleId = 1,
            moduleImageUrl = "",
            moduleName = "DHT11",
            moduleDescription = "Temperature and Humidity acquisition"
        ),
        Module(
            moduleId = 2,
            moduleImageUrl = "",
            moduleName = "PC",
            moduleDescription = "Configure your PC"
        ),
        Module(
            moduleId = 0,
            moduleImageUrl = "",
            moduleName = "ADD",
            moduleDescription = "Click here to add a module"
        )
    )

    private val modulesMutableStateFlow: MutableStateFlow<List<Module>> = MutableStateFlow(modulesList)

    val modulesStateFlow: StateFlow<List<Module>> = modulesMutableStateFlow.asStateFlow()

}//END of ModuleRepository