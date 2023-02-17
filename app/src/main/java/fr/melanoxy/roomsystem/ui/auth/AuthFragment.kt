package fr.melanoxy.roomsystem.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.doOnDetach
import androidx.core.view.doOnNextLayout
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.AuthFragmentBinding
import fr.melanoxy.roomsystem.ui.modules.ModulesFragment
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {

    private val binding by viewBinding { AuthFragmentBinding.bind(it) }
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get email inputs after a setOnFocusChangedToNotFocused to check if user exist on Firebase
        binding.authFragmentTietEmail.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){viewModel.onEmailComplete(binding.authFragmentTietEmail.text.toString())}
        }

        //get password and email in realtime
        binding.authFragmentTietEmail.doOnTextChanged { text, _, _, _ -> viewModel.onEmailChanged(text.toString()) }
        binding.authFragmentTietPswd.doOnTextChanged { text, _, _, _ -> viewModel.onPswrdChanged(text.toString()) }

        //Enable/disable depending userInput && change text depending FirebaseAnswer
        viewModel.buttonAuthStateItemLiveData.observe(viewLifecycleOwner) { value ->
            value?.let {
                binding.authFragmentButtonSignin.isEnabled = it.isEnable
                binding.authFragmentButtonSignin.setText(it.type)
            }
        }

        //listen click on authButton
        binding.authFragmentButtonSignin.setOnClickListener {
            viewModel.onSignInButtonClicked()
        }

        //Go to moduleFragment
        viewModel.singleLiveEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                SignEvent.NavigateToModuleFrag -> switchFragment()
            }
        }
    }

    private fun switchFragment() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_FrameLayout_container_modules, ModulesFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}