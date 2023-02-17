package fr.melanoxy.roomsystem.data.uiWidget

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class uiWidgetRepository @Inject constructor() {

    val errorMessageSateFlow = MutableStateFlow<String?>(null)

}//END of uiWidgetRepository