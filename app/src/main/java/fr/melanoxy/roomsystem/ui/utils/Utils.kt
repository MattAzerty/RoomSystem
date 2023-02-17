package fr.melanoxy.roomsystem.ui.utils

fun isEmailValid(email: String?): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email?.matches(emailPattern.toRegex()) ?: false
}