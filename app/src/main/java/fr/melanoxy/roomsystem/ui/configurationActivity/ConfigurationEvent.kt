package fr.melanoxy.roomsystem.ui.configurationActivity

sealed class ConfigurationEvent{
    object RequestPermissions : ConfigurationEvent()
    object EnableBluetooth : ConfigurationEvent()
    object ResetTerminal : ConfigurationEvent()
    object EndOfBTCom : ConfigurationEvent()
}