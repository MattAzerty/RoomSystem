package fr.melanoxy.roomsystem.ui.dht11SensorModActivity.dht11MainViewFragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.Dht11MainviewFragmentBinding
import fr.melanoxy.roomsystem.ui.utils.viewBinding
import java.lang.Float.max
import java.lang.Float.min

@AndroidEntryPoint
class DHT11MvFragment : Fragment(R.layout.dht11_mainview_fragment) {

    private val binding by viewBinding { Dht11MainviewFragmentBinding.bind(it) }
    private val viewModel by viewModels<DHT11MvViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeActionBarTitle()
        viewModel.setIsUserConnected(true)

        viewModel.realTimeTemperatureLiveData.observe(viewLifecycleOwner) {
            binding.dht11MainviewAnimationView.speed = 2f
            val previous = binding.dht11MainviewAnimationView.progress
            val progress =((it/100)+0.35).toFloat()

            binding.dht11MainviewAnimationView.setMinAndMaxProgress(min(previous, progress),max(previous, progress))
            if(progress<previous) binding.dht11MainviewAnimationView.speed = -1f else binding.dht11MainviewAnimationView.speed = 1f
            binding.dht11MainviewAnimationView.playAnimation()
        }

    }

    private fun changeActionBarTitle() {
        // Access the activity's ActionBar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        // Set the title of the ActionBar
        actionBar?.title = "DHT11 Module"
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeAsUpIndicator(R.drawable.baseline_memory_white_24dp)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setIsUserConnected(false)
    }


}//END of DHT11MvFragment


