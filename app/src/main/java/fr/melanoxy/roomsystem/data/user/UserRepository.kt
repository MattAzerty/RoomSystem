package fr.melanoxy.roomsystem.data.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import fr.melanoxy.roomsystem.data.FirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firebaseHelper: FirebaseHelper
) {//END of userRepository

    //private val USER_COLLECTION = "users"
    //private val db = FirebaseFirestore.getInstance()
    //private val userCollectionReference = db.collection(USER_COLLECTION)
    //val firebaseUser = Firebase.auth.currentUser//Current user logged
    val emailExistOnFirebaseStateFlow = MutableStateFlow<Boolean?>(null)

//CHECK IF USER AUTHENTICATED IN FIREBASE
    fun isUserAuthenticatedInFirebase(): Boolean {
            return firebaseHelper.getCurrentUser()!=null
    }

//CHECK IF EMAIL ALREADY EXIST ON FIRESTORE
    suspend fun isEmailAlreadyExistInFirebase(email: String) {
        try {
            // Make firebase request using a blocking call with Coroutine help
                emailExistOnFirebaseStateFlow.emit(
                firebaseHelper.auth.fetchSignInMethodsForEmail(email).await().signInMethods!!.isNotEmpty())
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            throw UserRepositoryError("Unable to check if email exist", cause)
        }
    }

//SIGN UP/IN (NEW) USER
    suspend fun connectUserOnFirebase(email:String, pswrd:String, isEmailExist:Boolean) {
    try {
        if(isEmailExist){
            firebaseHelper.auth.signInWithEmailAndPassword(email, pswrd).await()
            checkIfUserDocumentExist()
        } else {
            //new user
            firebaseHelper.auth.createUserWithEmailAndPassword(email, pswrd).await()
            checkIfUserDocumentExist()
        }
    } catch (cause: Throwable) {
        // If anything throws an exception, inform the caller
        throw UserRepositoryError("Unable to sign user", cause)
    }
}

    private suspend fun checkIfUserDocumentExist() {
        firebaseHelper.getUserDocumentReferenceOnFirestore()?.get()?.await()?.let { userDocument ->
            if (!userDocument.exists()) {
                createUserOnFirestore()
            }
    }}

    private fun createUserOnFirestore() {

        val modulesList: List<Int> = ArrayList()
        (modulesList as ArrayList<Int>).add(0)
        if (firebaseHelper.getCurrentUser() != null) {
            storeUserOnFirestore(
                User(
                    uid = firebaseHelper.getCurrentUser()!!.uid,
                    email = firebaseHelper.getCurrentUser()!!.email.toString(),
                    modules = modulesList
                )
            )
        }
    }

    private fun storeUserOnFirestore(user:User) {
        firebaseHelper.getUserDocumentReferenceOnFirestore()?.set(user)

    }

    fun getUserId(): String {
        return firebaseHelper.getCurrentUser()!!.uid
    }


    //EXCEPTION
    class UserRepositoryError(message: String, cause: Throwable?) : Throwable(message, cause)

}