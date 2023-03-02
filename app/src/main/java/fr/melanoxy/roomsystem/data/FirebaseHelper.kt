package fr.melanoxy.roomsystem.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseHelper @Inject constructor() {
//FIRESTORE
val auth = FirebaseAuth.getInstance()
private val db = FirebaseFirestore.getInstance()
private val usersCollection = db.collection("users")
//REAL TIME DATABASE
private val database = Firebase.database.reference

fun getUserDocumentReferenceOnFirestore(): DocumentReference? {
    return auth.currentUser?.let { usersCollection.document(it.uid) }
}

    fun getDatabaseRef(): DatabaseReference {
        return database
    }

    fun getUserUid(): String? {
    return auth.currentUser?.uid
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}