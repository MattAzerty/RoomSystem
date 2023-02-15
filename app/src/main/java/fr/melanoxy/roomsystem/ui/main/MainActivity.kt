package fr.melanoxy.roomsystem.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
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
//At start check if user is authenticated
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.mainFrameLayoutContainerModules.id,
                    if(!viewModel.isUserAuthenticated()) AuthFragment() else ModulesFragment()
                )
                .commitNow()
        }

    }
}//END of MainActivity