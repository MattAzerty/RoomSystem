package fr.melanoxy.roomsystem.ui.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.user.UserRepository
import fr.melanoxy.roomsystem.ui.utils.CoroutineDispatcherProvider
import fr.melanoxy.roomsystem.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    sharingRepository: SharingRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {//END of MainActivityViewModel

    val errorMessageFlow = sharingRepository.errorMessageSateFlow
    val singleLiveEvent = SingleLiveEvent<MainEvent>()

    init {//TODO: do not collect flow here because we would collect the flow even when user put the application on background
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            errorMessageFlow.collect { errorMessage ->
                withContext(coroutineDispatcherProvider.main) {
                    if(errorMessage!=null)
                singleLiveEvent.value = MainEvent.ShowSnackBarMessage(errorMessage) }
                errorMessageFlow.value=null
            }
        }
    }

    // Can't use viewModelScope.launch{}, because we would collect the flow even when user put the application on background
    /*val mainViewActionLiveData: LiveData<Event<MainViewAction>> =
        currentMailIdRepository.currentMailIdChannel.asLiveDataEvent(Dispatchers.IO) {
            if (!isTablet) {
                emit(MainViewAction.NavigateToDetailActivity)
            }
        }*/


//Check on Firebase if user is authenticated
    fun isUserAuthenticated():Boolean {return userRepository.isUserAuthenticatedInFirebase()}

//GETTERS for mainActivity

}