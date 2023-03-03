package fr.melanoxy.roomsystem.data.module

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import fr.melanoxy.roomsystem.data.FirebaseHelper
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.user.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepository @Inject constructor(
    private val sharingRepository: SharingRepository,
    private val firebaseHelper: FirebaseHelper
) {
    private val FIRESTORE_FIELD_NAME_MODULES_LIST = "modules"

    val modulesList = listOf(
        Module(
            moduleId = 0,
            moduleImageUrl = "",
            moduleName = "ADD",
            moduleDescription = "Click here to add a module",
            moduleSteps = "NA",
            moduleRequirement = "NA"
        ),
        Module(
            moduleId = 1,
            moduleImageUrl = "",
            moduleName = "DHT11",
            moduleDescription = "Temperature and Humidity acquisition",
            moduleRequirement = "- BLUETOOTH\n\n- INTERNET",
            moduleSteps = "1. Enter the Number for your Wi-Fi\n\n2. Provide the Wi-Fi password"
        ),
        Module(
            moduleId = 2,
            moduleImageUrl = "",
            moduleName = "PC",
            moduleDescription = "Configure your PC",
            moduleSteps = "tbd",
            moduleRequirement = "tbd"
        )

    )

    fun registerModule(moduleNumber: Int) {
        //create RDB entry for ESP32
        setOnRTDBIsUserConnected(true)
        //Add module into user module list
        firebaseHelper.getUserDocumentReferenceOnFirestore()?.update(
            FIRESTORE_FIELD_NAME_MODULES_LIST,
            FieldValue.arrayUnion(moduleNumber)
        ) ?: run {//case if ref is null
            showErrorMessage(moduleNumber)
        }

    }

    fun setOnRTDBIsUserConnected(isUserConnected: Boolean) {
        firebaseHelper.getUserUid()?.let {
            firebaseHelper.getDatabaseRef().child("UsersData").child("HTSensor")
                .child(it).child("isUserConnected").setValue(isUserConnected)
        } ?: run {//case if ref is null
            showErrorMessage(1)
        }
    }

    private fun showErrorMessage(moduleNumber: Int) {
        sharingRepository.errorMessageSateFlow.value = "error on module #$moduleNumber creation"
    }

    suspend fun getUserModules(): StateFlow<List<Int>> {

        var modulesList: List<Int> = listOf()//empty list

        firebaseHelper.getUserDocumentReferenceOnFirestore()?.get()?.await()?.let { userDocument ->
            if (userDocument.exists()) {
                val user = userDocument.toObject(User::class.java)
                modulesList = user!!.modules
            }
        }

        return MutableStateFlow(modulesList)
    }

    fun getRealTimeTemperatureFlow(): Flow<Double> = callbackFlow {
        // Define a listener to handle changes in the value
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? Double // Get the value from the snapshot
                // Offer the value to the flow
                trySend(value ?: 0.0)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                close(error.toException())
            }
        }

        // Add the listener to the database reference to start listening for changes
        firebaseHelper.getUserUid()?.let {
            firebaseHelper.getDatabaseRef().child("UsersData").child("HTSensor").child(it)
                .child("realTimeTEMPERATURE").addValueEventListener(valueEventListener)

        }

        // Define a cancellation block to remove the listener when the flow is cancelled
        awaitClose {
            firebaseHelper.getUserUid()?.let {
            firebaseHelper.getDatabaseRef().child("UsersData").child("HTSensor").child(it)
                .child("realTimeTEMPERATURE").removeEventListener(valueEventListener)
            }
        }
    }
}