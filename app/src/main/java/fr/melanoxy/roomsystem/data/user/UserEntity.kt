package fr.melanoxy.roomsystem.data.user

data class UserEntity(
    val uid: String,
    val username: String,
    val email: String,
    val dht11: String,
    val ipAddress: String
)
