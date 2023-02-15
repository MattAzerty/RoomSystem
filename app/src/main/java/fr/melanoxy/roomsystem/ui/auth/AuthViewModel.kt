package fr.melanoxy.roomsystem.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
}