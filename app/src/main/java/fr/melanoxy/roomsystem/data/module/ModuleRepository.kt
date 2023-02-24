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
            moduleDescription = "Temperature and Humidity acquisition",
            moduleRequirement = "- BLUETOOTH\n\n- INTERNET",
            moduleSteps = "1. Enter the Number for your Wi-Fi\n\n2. Provide the Wi-Fi password"
        ),
        Module(
            moduleId = 2,
            moduleImageUrl = "",
            moduleName = "PC",
            moduleDescription = "Configure your PC",
            moduleSteps = "tbd",
            moduleRequirement = "tbd"
        ),
        Module(
            moduleId = 0,
            moduleImageUrl = "",
            moduleName = "ADD",
            moduleDescription = "Click here to add a module",
            moduleSteps = "NA",
            moduleRequirement = "NA"
        )
    )

    val modulesStateFlow: StateFlow<List<Module>> = MutableStateFlow(modulesList)

}//END of ModuleRepository