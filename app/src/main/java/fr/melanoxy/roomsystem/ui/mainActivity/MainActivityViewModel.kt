package fr.melanoxy.roomsystem.ui.mainActivity

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

    private val errorMessageFlow = sharingRepository.errorMessageSateFlow
    private val moduleClickedFlow = sharingRepository.moduleClickedSateFlow
    val singleLiveEvent = SingleLiveEvent<MainEvent>()

    init {//TODO: Use Mediator?
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            errorMessageFlow.collect { errorMessage ->
                withContext(coroutineDispatcherProvider.main) {
                    if(errorMessage!=null)
                singleLiveEvent.value = MainEvent.ShowSnackBarMessage(errorMessage) }
                errorMessageFlow.value=null
            }
        }
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            moduleClickedFlow.collect { int ->
                withContext(coroutineDispatcherProvider.main) {
                    if(int==0)
                        singleLiveEvent.value = MainEvent.LaunchConfigurationActivity }
                moduleClickedFlow.value=null
            }
        }
        }

//Check on Firebase if user is authenticated
    fun isUserAuthenticated():Boolean {return userRepository.isUserAuthenticatedInFirebase()}

//GETTERS for mainActivity

}