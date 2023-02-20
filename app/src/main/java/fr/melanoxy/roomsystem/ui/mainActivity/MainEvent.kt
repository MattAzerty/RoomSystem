package fr.melanoxy.roomsystem.ui.mainActivity

sealed class MainEvent{
   data class ShowSnackBarMessage(val message: String) : MainEvent()
   object LaunchConfigurationActivity : MainEvent()
}
