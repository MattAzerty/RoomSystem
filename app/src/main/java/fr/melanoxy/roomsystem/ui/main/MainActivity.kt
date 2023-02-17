package fr.melanoxy.roomsystem.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.databinding.ActivityMainBinding
import fr.melanoxy.roomsystem.ui.auth.AuthFragment
import fr.melanoxy.roomsystem.ui.modules.ModulesFragment
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

// Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.singleLiveEvent.observe(this) { event ->
            when (event) {
                is MainEvent.ShowSnackBarMessage -> Snackbar.make(binding.mainCl, event.message, Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}//END of MainActivity