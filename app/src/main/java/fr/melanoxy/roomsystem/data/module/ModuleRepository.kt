package fr.melanoxy.roomsystem.data.module

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepository @Inject constructor() {

    private val modulesMutableStateFlow: MutableStateFlow<List<Module>> = MutableStateFlow(
        listOf(
            Module(
                moduleId = 1,
                moduleImageUrl = "",
                moduleName = "DHT11"

            ),
            Module(
                moduleId = 0,
                moduleImageUrl = "",
                moduleName = "ADD"
            )
        )
    )

    val modulesStateFlow: StateFlow<List<Module>> = modulesMutableStateFlow.asStateFlow()

}//END of ModuleRepository