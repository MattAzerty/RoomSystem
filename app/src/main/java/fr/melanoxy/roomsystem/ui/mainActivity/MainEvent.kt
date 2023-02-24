package fr.melanoxy.roomsystem.ui.mainActivity

sealed class MainEvent{
   data class ShowSnackBarMessage(val message: String) : MainEvent()
   data class LaunchActivity(val moduleId: Int) : MainEvent()
}
