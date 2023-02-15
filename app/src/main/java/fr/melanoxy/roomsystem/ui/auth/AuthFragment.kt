package fr.melanoxy.roomsystem.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.AuthFragmentBinding
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {

    private val binding by viewBinding { AuthFragmentBinding.bind(it) }
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}