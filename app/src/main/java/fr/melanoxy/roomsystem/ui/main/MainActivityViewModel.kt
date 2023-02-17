package fr.melanoxy.roomsystem.ui.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.uiWidget.uiWidgetRepository
import fr.melanoxy.roomsystem.data.user.UserRepository
import fr.melanoxy.roomsystem.ui.utils.CoroutineDispatcherProvider
import fr.melanoxy.roomsystem.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    uiWidgetRepository: uiWidgetRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {//END of MainActivityViewModel

    val errorMessageFlow = uiWidgetRepository.errorMessageSateFlow
    val singleLiveEvent = SingleLiveEvent<MainEvent>()

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            errorMessageFlow.collect { errorMessage ->
                withContext(coroutineDispatcherProvider.main) {
                    if(errorMessage!=null)
                singleLiveEvent.value = MainEvent.ShowSnackBarMessage(errorMessage) }
                errorMessageFlow.value=null
            }
        }
    }




//Check on Firebase if user is authenticated
    fun isUserAuthenticated():Boolean {return userRepository.isUserAuthenticatedInFirebase()}

//GETTERS for mainActivity

}