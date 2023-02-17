package fr.melanoxy.roomsystem.data.user

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.melanoxy.roomsystem.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {//END of userRepository

    val emailExistOnFirebaseStateFlow = MutableStateFlow<Boolean?>(null)
    val userStateFlow = MutableStateFlow<User?>(null)

//CHECK IF USER AUTHENTICATED IN FIREBASE
    fun isUserAuthenticatedInFirebase(): Boolean {
            return Firebase.auth.currentUser!=null
    }

//CHECK IF EMAIL ALREADY EXIST ON FIRESTORE
    suspend fun isEmailAlreadyExistInFirebase(email: String) {
        try {
            // Make firebase request using a blocking call with Coroutine help
                emailExistOnFirebaseStateFlow.emit(
                    Firebase.auth.fetchSignInMethodsForEmail(email).await().signInMethods!!.isNotEmpty())
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            throw UserRepositoryError("Unable to check if email exist", cause)
        }
    }

//SIGN UP/IN (NEW) USER
    suspend fun connectUserOnFirebase(email:String, pswrd:String, type:Boolean) {
    try {
        if(type) Firebase.auth.signInWithEmailAndPassword(email, pswrd).await() else
         Firebase.auth.createUserWithEmailAndPassword(email, pswrd).await()
    } catch (cause: Throwable) {
        // If anything throws an exception, inform the caller
        throw UserRepositoryError("Unable to sign user", cause)
    }
}

//EXCEPTION
    class UserRepositoryError(message: String, cause: Throwable?) : Throwable(message, cause)

}//END of userRepository