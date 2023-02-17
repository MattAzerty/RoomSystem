package fr.melanoxy.roomsystem.ui.main

sealed class MainEvent{
   data class ShowSnackBarMessage(val message: String) : MainEvent()
}
