package fr.melanoxy.roomsystem.ui.mainActivity

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.user.UserRepository
import fr.melanoxy.roomsystem.ui.utils.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharingRepository: SharingRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {//END of MainActivityViewModel


// Can't use viewModelScope.launch{}, because we would collect the flow even when user put the application on background
    private val moduleIdLiveData: LiveData<Event<Int>> = sharingRepository.currentModuleIdChannel.asLiveDataEvent(Dispatchers.IO) {
        emit(it)
}
    private val errorMessageLiveData = sharingRepository.errorMessageSateFlow.asLiveData()//TODO do like moduleIdLiveData

    //MEDIATOR (moduleId+errorMessage)
    private val mediatorLiveData = MediatorLiveData<MainEvent>()
    init {
        mediatorLiveData.addSource(moduleIdLiveData) { moduleId -> combine(moduleId, errorMessageLiveData.value)}
        mediatorLiveData.addSource(errorMessageLiveData) { errorMessage -> combine(moduleIdLiveData.value, errorMessage)}
    }

    private fun combine(event: Event<Int>?, errorMessage: String?) {
        event?.handleContent { int -> mediatorLiveData.value = MainEvent.LaunchActivity(int) }
        if (errorMessage!=null) mediatorLiveData.value = MainEvent.ShowSnackBarMessage(errorMessage)
        sharingRepository.errorMessageSateFlow.value=null
    }

    //GETTERS FOR FRAGMENT
    val mainEventLiveData: LiveData<MainEvent>
        get() = mediatorLiveData





//TODO COLLECT FLOW FOR LIST MODULE FROM FIREBASE and USER DOCUMENT
    /*init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            errorMessageFlow.collect { errorMessage ->
                withContext(coroutineDispatcherProvider.main) {
                    if(errorMessage!=null)
                singleLiveMainEvent.value = MainEvent.ShowSnackBarMessage(errorMessage) }
                errorMessageFlow.value=null
            }
        }
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            moduleClickedFlow.collect { int ->
                withContext(coroutineDispatcherProvider.main) {
                    if(int==0)
                        singleLiveMainEvent.value = MainEvent.LaunchConfigurationActivity }
                moduleClickedFlow.value=null
            }
        }
        }*/

//Check on Firebase if user is authenticated
    fun isUserAuthenticated():Boolean {return userRepository.isUserAuthenticatedInFirebase()}

//GETTERS for mainActivity

}