package fr.melanoxy.roomsystem.data.module

import com.google.firebase.firestore.FieldValue
import fr.melanoxy.roomsystem.data.FirebaseHelper
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        ),
        Module(
            moduleId = 0,
            moduleImageUrl = "",
            moduleName = "ADD",
            moduleDescription = "Click here to add a module",
            moduleSteps = "NA",
            moduleRequirement = "NA"
        )
    )

    val modulesStateFlow: StateFlow<List<Module>> = MutableStateFlow(modulesList)

    fun registerModule(moduleNumber: Int) {
        //create RDB entry for ESP32
        firebaseHelper.getUserUid()?.let {
            firebaseHelper.getDatabaseRef().child("UsersData").child("HTSensor")
                .child(it).child("isUserConnected").setValue(false)
        }?: run {//case if ref is null
            showErrorMessage(moduleNumber)
        }
        //Add module into user module list
        firebaseHelper.getUserDocumentReferenceOnFirestore()?.update(
            FIRESTORE_FIELD_NAME_MODULES_LIST,
            FieldValue.arrayUnion(moduleNumber)
        ) ?: run {//case if ref is null
            showErrorMessage(moduleNumber)
        }

    }

    private fun showErrorMessage(moduleNumber: Int) {
        sharingRepository.errorMessageSateFlow.value="error on module #$moduleNumber creation"
    }

}