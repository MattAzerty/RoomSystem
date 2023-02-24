package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

sealed class SelectionConfigEvent{
    object LaunchReadmeConfigFragment : SelectionConfigEvent()
    data class LaunchSelectedConfigFragment(val moduleSelected:Int) : SelectionConfigEvent()
}