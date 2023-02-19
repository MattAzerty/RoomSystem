package fr.melanoxy.roomsystem.ui.authFragment

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.user.UserRepository
import fr.melanoxy.roomsystem.ui.utils.CoroutineDispatcherProvider
import fr.melanoxy.roomsystem.ui.utils.SingleLiveEvent
import fr.melanoxy.roomsystem.ui.utils.isEmailValid
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharingRepository: SharingRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val emailMutableLiveData = MutableLiveData<String>()
    private val pswrdMutableLiveData = MutableLiveData<String>()
    private val existLiveData = userRepository.emailExistOnFirebaseStateFlow.asLiveData()

    val singleLiveEvent = SingleLiveEvent<SignEvent>()

    private val mediatorLiveData = MediatorLiveData<ButtonAuthStateItem>()
    init {
        mediatorLiveData.addSource(existLiveData) { exist -> combine(exist, emailMutableLiveData.value, pswrdMutableLiveData.value)}
        mediatorLiveData.addSource(emailMutableLiveData) { email -> combine(existLiveData.value, email, pswrdMutableLiveData.value)}
        mediatorLiveData.addSource(pswrdMutableLiveData) { pswrd -> combine(existLiveData.value, emailMutableLiveData.value, pswrd) }
    }

    private fun combine(exist: Boolean?, email: String?, pswrd: String?) {
        mediatorLiveData.value = ButtonAuthStateItem(
            isEnable = pswrd != null && pswrd.length>5 && isEmailValid(email),
            type = if (exist!=null && exist) R.string.connect else R.string.create
        )
    }

//GETTERS FOR FRAGMENT
    val buttonAuthStateItemLiveData: LiveData<ButtonAuthStateItem>
        get() = mediatorLiveData


// ON BEHAVIOR FUNCTIONS
    fun onEmailComplete(email: String?) {
        if(email!=null && isEmailValid(email)){
            viewModelScope.launch(coroutineDispatcherProvider.io) {
                try {
                    userRepository.isEmailAlreadyExistInFirebase(email)
                } catch (error: UserRepository.UserRepositoryError) {
                    sharingRepository.errorMessageSateFlow.value=error.message
                } finally {
                    //_spinner.value = false
                }
            }

        }
    }

    fun onEmailChanged(email: String) {
        emailMutableLiveData.value = email
    }

    fun onPswrdChanged(pswrd: String) {
        pswrdMutableLiveData.value = pswrd
    }

    fun onSignInButtonClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            try {
                userRepository.connectUserOnFirebase(
                    emailMutableLiveData.value ?: "",
                    pswrdMutableLiveData.value ?: "",
                    existLiveData.value ?: false
                )
                // If execution reaches this point, no exception was thrown//SignEvent.NavigateToModuleFrag)
                withContext(coroutineDispatcherProvider.main) {
                singleLiveEvent.value=SignEvent.NavigateToModuleFrag}
            } catch (error: UserRepository.UserRepositoryError) {
                sharingRepository.errorMessageSateFlow.value=error.message
            } finally {
                //_spinner.value = false
            }
    }}

}