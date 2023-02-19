package fr.melanoxy.roomsystem.ui.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.databinding.ActivityMainBinding
import fr.melanoxy.roomsystem.ui.authFragment.AuthFragment
import fr.melanoxy.roomsystem.ui.modulesFragment.ModulesFragment
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

// Show a snackbar whenever the [ViewModel.singleLiveEvent] is updated
        viewModel.singleLiveEvent.observe(this) { event ->
            when (event) {
                is MainEvent.ShowSnackBarMessage -> Snackbar.make(binding.mainCl, event.message, Snackbar.LENGTH_SHORT).show()
            }
        }

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