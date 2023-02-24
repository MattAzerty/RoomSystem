package fr.melanoxy.roomsystem.data.activityCrossFragment

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharingRepository @Inject constructor() {

    private val currentModuleIdMutableSharedFlow = MutableStateFlow<Int?>(null)
    val currentModuleIdChannel = Channel<Int>()
    val currentModuleIdFlow: StateFlow<Int?> = currentModuleIdMutableSharedFlow.asStateFlow()

    fun setCurrentModuleId(currentId: Int) {
        currentModuleIdMutableSharedFlow.value = currentId
        currentModuleIdChannel.trySend(currentId)
    }


    val errorMessageSateFlow = MutableStateFlow<String?>(null)

}//END of uiWidgetRepository