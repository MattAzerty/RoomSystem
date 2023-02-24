package fr.melanoxy.roomsystem.data.module

import java.io.Serializable

data class Module (
    val moduleName: String,
    val moduleImageUrl: String,
    val moduleId: Int,
    val moduleDescription: String,
    val moduleRequirement: String,
    val moduleSteps: String
) : Serializable