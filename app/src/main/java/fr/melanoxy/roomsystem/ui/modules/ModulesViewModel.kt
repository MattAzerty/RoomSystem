package fr.melanoxy.roomsystem.ui.modules

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class ModulesViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
}