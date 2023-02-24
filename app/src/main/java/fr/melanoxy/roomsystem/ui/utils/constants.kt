package fr.melanoxy.roomsystem.ui.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

const val SPP_UUID = "00001101-0000-1000-8000-00805f9b34fb"// ou "SerialPortServiceClass_UUID"
@RequiresApi(Build.VERSION_CODES.S)
val PERMISSIONS = arrayOf(//For BT feature
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.BLUETOOTH_SCAN,

)