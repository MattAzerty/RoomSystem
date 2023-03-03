package fr.melanoxy.roomsystem.data.user

import java.io.Serializable
data class User(
    val uid: String,
    val email: String,
    val modules: List<Int>
): Serializable{
constructor() : this("", "", emptyList())//FOR Deserialisation
}
