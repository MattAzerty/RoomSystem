package fr.melanoxy.roomsystem.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    private var isAuthenticated: Boolean = userRepository.isUserAuthenticatedInFirebase()

    fun isUserAuthenticated(): Boolean {

        return isAuthenticated
    }

}//END of MainActivityViewModel