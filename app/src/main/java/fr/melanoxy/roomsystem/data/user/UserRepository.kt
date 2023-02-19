package fr.melanoxy.roomsystem.data.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    private val USER_COLLECTION = "users"
    private val db = FirebaseFirestore.getInstance()
    private val userCollectionReference = db.collection(USER_COLLECTION)
    val emailExistOnFirebaseStateFlow = MutableStateFlow<Boolean?>(null)

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
    suspend fun connectUserOnFirebase(email:String, pswrd:String, isEmailExist:Boolean) {
    try {
        if(isEmailExist) Firebase.auth.signInWithEmailAndPassword(email, pswrd).await() else {
            //new user
         Firebase.auth.createUserWithEmailAndPassword(email, pswrd).await()
         createUserOnFirestore()
        }
    } catch (cause: Throwable) {
        // If anything throws an exception, inform the caller
        throw UserRepositoryError("Unable to sign user", cause)
    }
}

    private fun createUserOnFirestore() {
        val firebaseUser = Firebase.auth.currentUser//Current user logged
        val modulesList: List<Int> = ArrayList()
        if (firebaseUser != null) {
            storeUserOnFirestore(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email.toString(),
                    modules = modulesList
                )
            )
        }
    }

    private fun storeUserOnFirestore(user:User) {
        userCollectionReference.document(user.uid).set(user)
    }


    //EXCEPTION
    class UserRepositoryError(message: String, cause: Throwable?) : Throwable(message, cause)

}//END of userRepository