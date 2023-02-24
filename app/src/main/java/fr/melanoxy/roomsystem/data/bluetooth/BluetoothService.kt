package fr.melanoxy.roomsystem.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import fr.melanoxy.roomsystem.ui.utils.PERMISSIONS
import fr.melanoxy.roomsystem.ui.utils.SPP_UUID
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


//https://developer.android.com/guide/topics/connectivity/bluetooth
//..Classic Bluetooth
@Singleton
class BluetoothService @Inject constructor(
    private val context: Context
) {

    var isConnectedMutableStateFlow:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val putTxt = MutableSharedFlow<String>(onBufferOverflow = BufferOverflow.SUSPEND)//in order to not lose values on UI

    private var deviceFound = false
    private val scope = CoroutineScope(Dispatchers.IO)
    private var bluetoothDevice:BluetoothDevice? = null

//FIND
    private val mBluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val mBluetoothAdapter: BluetoothAdapter? = mBluetoothManager.adapter
    val isBluetoothSupport = mBluetoothAdapter!=null
    //Check if device is compatible with BT before check if it's enabled
    val isBluetoothEnable = mBluetoothAdapter?.isEnabled
    private var mBluetoothStateReceiver: BroadcastReceiver? = null
//CONNECT
private var socket: BluetoothSocket? = null

    fun isAppHasAllPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        } else true
    }


    @SuppressLint("MissingPermission")
    suspend fun scanDevice(deviceNameToScan: String) {
        //deviceFound = false
        putTxt.emit( "Looking for paired devices...\n")
        //Query paired devices
        val pairedDevices = mBluetoothAdapter?.bondedDevices
        bluetoothDevice = pairedDevices?.find{ device -> (device.name.length > 4 && device.name.substring(0, 3) == deviceNameToScan) }

        if (bluetoothDevice!=null) {
            deviceFound=true
        } else {putTxt.emit("No paired device found.\n") }

        registerBluetoothReceiver()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @SuppressLint("MissingPermission")
    fun registerBluetoothReceiver(){

        //Registering Action to stateFilter
        val stateFilter = IntentFilter()
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED) //BluetoothAdapter.ACTION_STATE_CHANGED : Changing state of BT
        stateFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        stateFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED) //Verify connection
        stateFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED) //Verify disconnection
        stateFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        stateFilter.addAction(BluetoothDevice.ACTION_FOUND) // Register for broadcasts when a device is discovered.
        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED) //Discovery of the device Started
        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) //Discovery of the device Finished
        stateFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)

        // Create a BroadcastReceiver for ACTION
        val receiver = object : BroadcastReceiver() {

            @SuppressLint("MissingPermission")
            override fun onReceive(context: Context, intent: Intent) {
                when(intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        bluetoothDevice =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        val deviceName = bluetoothDevice?.name
                        if (!deviceFound && deviceName != null && deviceName.length > 4 && deviceName.substring(0, 3) == "ESP") {
                            putTxt.tryEmit("Device found...\n")
                            deviceFound=true
                            // Cancel discovery because it otherwise slows down the connection.
                            mBluetoothAdapter?.cancelDiscovery()
                            scope.launch {  connectToTargetedDevice(bluetoothDevice)}
                        }
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        // Discovery has found a device. Get the BluetoothDevice
                        putTxt.tryEmit("Discovering devices...\n")
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        if (deviceFound) putTxt.tryEmit("Discovery finished...\n") else
                            putTxt.tryEmit("Unable to found the device.\n")

                    }
                    BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        isConnectedMutableStateFlow.value = false
                    }
                }
            }
        }

        // Register for broadcasts for action in stateFilter
        context.registerReceiver(receiver, stateFilter)
        if(deviceFound) scope.launch {connectToTargetedDevice(bluetoothDevice)}
        else mBluetoothAdapter!!.startDiscovery()//Start looking for nearby BT devices
    }


    private suspend fun receiveData() {

        try {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (true) {
                withContext(Dispatchers.IO) {
                    bytesRead = socket?.inputStream?.read(buffer) ?:-1
                }
                val data = buffer.copyOf(bytesRead)
                val s = String(data)
                putTxt.emit(s)
            }
        } catch (e: Exception) {
            isConnectedMutableStateFlow.value = false
            try {
                socket!!.close()
                scope.cancel()
            } catch (ignored: Exception) {
            }
            socket = null
        }
    }

    @SuppressLint("MissingPermission")
    @ExperimentalUnsignedTypes
    suspend fun connectToTargetedDevice(targetDevice: BluetoothDevice?) {
        //phone connect as a client for the remote device that is accepting connections on an open server socket
        putTxt.emit("Connecting to ...${targetDevice?.name}\n")
        //bluetooth device object with serial connection
        val uuid = UUID.fromString(SPP_UUID)
        socket = targetDevice?.createRfcommSocketToServiceRecord(uuid)
        val result = connectDeviceTroughSocket(socket)//blockingCall
        if(result.success) receiveData()
        else putTxt.emit(result.errorMessage!!)
        }

    @SuppressLint("MissingPermission")
    private suspend fun connectDeviceTroughSocket(mSocket: BluetoothSocket?): SocketResponse {

        putTxt.emit("Connecting to socket...\n")

        return runCatching {
            mSocket?.connect()//This call blocks until it succeeds or throws an exception.
            putTxt.emit("Connected to socket.\n")
            SocketResponse(success = true)

        }.getOrElse {
            SocketResponse(success = false, errorMessage = "Could not open the client socket\n")
        }
    }

    fun disconnect() {
        try {
            socket?.close()
            //isConnectedMutableStateFlow.value = false
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun unregisterReceiver() {//TODO how to know powerConsumption?
        if(mBluetoothStateReceiver!=null) {
            context.unregisterReceiver(mBluetoothStateReceiver)
            mBluetoothStateReceiver = null
        }
        scope.cancel()
        disconnect()
    }

    suspend fun sendByteData(byteArrayToSend: ByteArray) {
        withContext(Dispatchers.IO) {
        socket?.outputStream?.write(byteArrayToSend)
        }
    }
}