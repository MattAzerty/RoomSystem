package fr.melanoxy.roomsystem.ui.dht11SensorModActivity.dht11MainViewFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.Dht11MainviewFragmentBinding
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class DHT11MvFragment : Fragment(R.layout.dht11_mainview_fragment) {

    private val binding by viewBinding { Dht11MainviewFragmentBinding.bind(it) }
    private val viewModel by viewModels<DHT11MvViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setIsUserConnected(true)

        viewModel.realTimeTemperatureLiveData.observe(viewLifecycleOwner) {
            binding.dht11MainviewFragmentTv.text = it.toString()

        }

    }

    override fun onStop() {
        super.onStop()
        viewModel.setIsUserConnected(false)
    }


}//END of DHT11MvFragment


