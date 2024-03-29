package fr.melanoxy.roomsystem.ui.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.databinding.ActivityMainBinding
import fr.melanoxy.roomsystem.ui.authFragment.AuthFragment
import fr.melanoxy.roomsystem.ui.configurationActivity.ConfigurationActivity
import fr.melanoxy.roomsystem.ui.dht11SensorModActivity.DHT11SensorActivity
import fr.melanoxy.roomsystem.ui.mainActivity.modulesFragment.ModulesFragment
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//At start check if user is authenticated else show Login view
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.mainFrameLayoutContainerModules.id,
                    if(!viewModel.isUserAuthenticated()) AuthFragment() else ModulesFragment()
                )
                .commitNow()
        }

//Event(s) observer
        viewModel.mainEventLiveData.observe(this) { event ->
            when (event) {
                is MainEvent.ShowSnackBarMessage -> Snackbar.make(binding.mainCl, event.message, Snackbar.LENGTH_SHORT).show()
                is MainEvent.LaunchActivity -> startMyActivity(event.moduleId)
            }
        }

    }

    private fun startMyActivity(moduleId: Int) {
        lateinit var i: Intent
        when(moduleId){
        0 -> i = Intent(this, ConfigurationActivity::class.java)
        1 -> i = Intent(this, DHT11SensorActivity::class.java)
        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle home button click
        if (item.itemId == android.R.id.home) {
            // Handle the click event here
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}//END of MainActivity