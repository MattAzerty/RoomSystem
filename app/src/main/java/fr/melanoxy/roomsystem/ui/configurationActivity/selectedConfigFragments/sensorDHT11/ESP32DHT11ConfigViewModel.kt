package fr.melanoxy.roomsystem.ui.configurationActivity.selectedConfigFragments.sensorDHT11

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.bluetooth.BluetoothService
import fr.melanoxy.roomsystem.data.module.ModuleRepository
import fr.melanoxy.roomsystem.data.user.UserRepository
import fr.melanoxy.roomsystem.ui.configurationActivity.ConfigurationEvent
import fr.melanoxy.roomsystem.ui.utils.CoroutineDispatcherProvider
import fr.melanoxy.roomsystem.ui.utils.Event
import fr.melanoxy.roomsystem.ui.utils.SingleLiveEvent
import fr.melanoxy.roomsystem.ui.utils.asLiveDataEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class ESP32DHT11ConfigViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val moduleRepository: ModuleRepository,
    private val bluetoothService: BluetoothService,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    val dataReceivedLiveData = liveData(Dispatchers.IO) {
        bluetoothService.putTxt.collect { dataReceived ->

            when{
                dataReceived.contains("userUID...") -> {
                    bluetoothService.sendByteData(userRepository.getUserId().toByteArray(Charset.defaultCharset()))
                    emit(dataReceived)
                }
                dataReceived.contains("failed") -> {
                    withContext(coroutineDispatcherProvider.main) {
                    bluetoothSingleLiveEvent.value = ConfigurationEvent.ResetTerminal}
                }
                dataReceived.contains("--END OF BT COM--") -> {
                    moduleRepository.registerModule(1)
                    withContext(coroutineDispatcherProvider.main) {
                    bluetoothSingleLiveEvent.value = ConfigurationEvent.EndOfBTCom}
                }
                else -> {emit(dataReceived)}
            }
        }
    }
    val bluetoothSingleLiveEvent = SingleLiveEvent<ConfigurationEvent>()

  fun startBTConnection() {
          if (bluetoothService.isBluetoothSupport && bluetoothService.isAppHasAllPermissions()) {   // Check Bluetooth Support and permissions
              if(bluetoothService.isBluetoothEnable == true){ // Check if Bluetooth is Enabled
                  //Progress Bar
                  //TODO: setInProgress(true)
                  //Start scanning devices
                  viewModelScope.launch(coroutineDispatcherProvider.io) {
                  bluetoothService.scanDevice("ESP")}
              }else{
                  // If Bluetooth is enabled but inactive
                  // Request user consent to turn ON Bluetooth
                  bluetoothSingleLiveEvent.value = ConfigurationEvent.EnableBluetooth
              }
          }
          else{ //Bluetooth is not supported.
              bluetoothSingleLiveEvent.value = ConfigurationEvent.RequestPermissions
              //TODO: "Bluetooth is not supported."
          }
  }

  fun onSendDataClicked(sendTxt: String) {
      viewModelScope.launch(coroutineDispatcherProvider.io) {
          bluetoothService.sendByteData(sendTxt.toByteArray(Charset.defaultCharset()))
      }
    }

  fun unregisterReceiver() {
      bluetoothService.unregisterReceiver()
  }

  fun onBTEnableRequestResult(result: ActivityResult) {
      when (result.resultCode){
          Activity.RESULT_OK -> startBTConnection()
          Activity.RESULT_CANCELED -> bluetoothSingleLiveEvent.value = ConfigurationEvent.EnableBluetooth
      }
  }


  fun isAppHasAllPermission(): Boolean {
      return bluetoothService.isAppHasAllPermissions()
  }

    /*fun refresh() {
        if(bluetoothService.isConnectedMutableStateFlow.value != true){startBTConnection()}
    }*/
}