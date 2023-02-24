package fr.melanoxy.roomsystem.ui.configurationActivity.selectedConfigFragments.sensorDHT11

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.Esp32Dht11FragmentBinding
import fr.melanoxy.roomsystem.ui.configurationActivity.ConfigurationEvent
import fr.melanoxy.roomsystem.ui.utils.PERMISSIONS
import fr.melanoxy.roomsystem.ui.utils.viewBinding


@AndroidEntryPoint
class ESP32DHT11ConfigFragment : Fragment(R.layout.esp32_dht11_fragment) {
    private val binding by viewBinding { Esp32Dht11FragmentBinding.bind(it) }
    private val viewModel by viewModels<ESP32DHT11ConfigViewModel>()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//REQUEST PERMISSIONS AT START if needed
        if (viewModel.isAppHasAllPermission()) viewModel.startBTConnection()
        else requestMultiplePermissions()

        startBluetoothScan()

    }


    override fun onStop() {
        super.onStop()
        viewModel.unregisterReceiver()
    }


    private fun startBluetoothScan() {

//SINGLE LIVE EVENT
        viewModel.bluetoothSingleLiveEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                ConfigurationEvent.RequestPermissions -> requestMultiplePermissions()
                ConfigurationEvent.EnableBluetooth -> enableBluetooth()
            }
        }

        viewModel.dataReceivedLiveData.observe(viewLifecycleOwner) {
            binding.esp32Dht11FragmentTxtRead.append(it)
        }

        binding.esp32Dht11FragmentSendBtn.setOnClickListener {
            viewModel.onSendDataClicked(binding.esp32Dht11FragmentSendText.text.toString().trim())
        }

    }

    private fun enableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startForResult.launch(enableBtIntent)
    }


//Result handler after on BT enable request
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult -> viewModel.onBTEnableRequestResult(result)
    }


    private fun requestMultiplePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Initialize the permission launcher for BT permissions
            permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                    permissions -> if (!permissions.containsValue(false)) viewModel.startBTConnection()
            }

            permissionLauncher.launch(PERMISSIONS)

        }
    }

}//END of ESP32DHT11ConfigFragment
