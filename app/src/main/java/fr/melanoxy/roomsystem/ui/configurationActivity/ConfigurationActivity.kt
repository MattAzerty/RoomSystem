package fr.melanoxy.roomsystem.ui.configurationActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.databinding.ActivityConfigurationBinding
import fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments.SelectionConfigFragment
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class ConfigurationActivity : AppCompatActivity() {

    private val binding by viewBinding { ActivityConfigurationBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.activityConfigurationFrameLayoutContainer.id, SelectionConfigFragment())
                .commitNow()
        }

    }
}//End of configuration activity