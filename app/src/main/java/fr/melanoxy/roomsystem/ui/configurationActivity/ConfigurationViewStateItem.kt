package fr.melanoxy.roomsystem.ui.configurationActivity

data class ConfigurationViewStateItem (
        val confId: Int,
        val confName: String,
        val confDescription: String,
        val onConfigurationClicked: () -> Unit,
    )