package fr.melanoxy.roomsystem.ui.configurationActivity.selectedConfigFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.Esp32Dht11FragmentBinding
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class ESP32DHT11Fragment : Fragment(R.layout.esp32_dht11_fragment) {
    private val binding by viewBinding { Esp32Dht11FragmentBinding.bind(it) }
    private val viewModel by viewModels<ESP32DHT11ViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
