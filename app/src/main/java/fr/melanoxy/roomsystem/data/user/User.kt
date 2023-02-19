package fr.melanoxy.roomsystem.data.user

data class User(
    val uid: String,
    val email: String,
    val modules: List<Int>
)
