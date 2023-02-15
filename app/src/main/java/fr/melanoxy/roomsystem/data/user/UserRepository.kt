package fr.melanoxy.roomsystem.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    private lateinit var auth: FirebaseAuth

    fun isUserAuthenticatedInFirebase(): Boolean {
        // Initialize Firebase Auth
        auth = Firebase.auth
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        return currentUser!=null
    }


}