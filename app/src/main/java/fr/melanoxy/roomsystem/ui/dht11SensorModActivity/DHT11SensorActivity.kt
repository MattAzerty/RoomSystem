package fr.melanoxy.roomsystem.ui.dht11SensorModActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.databinding.ActivityDht11SensorBinding
import fr.melanoxy.roomsystem.ui.dht11SensorModActivity.dht11MainViewFragment.DHT11MvFragment
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class DHT11SensorActivity : AppCompatActivity() {

    private val binding by viewBinding { ActivityDht11SensorBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.activityDht11FrameLayoutContainer.id, DHT11MvFragment())
                .commitNow()
        }
    }

}