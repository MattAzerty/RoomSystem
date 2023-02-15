package fr.melanoxy.roomsystem.ui.modules

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.ModulesFragmentBinding
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class ModulesFragment : Fragment(R.layout.modules_fragment) {

    private val binding by viewBinding { ModulesFragmentBinding.bind(it) }
    private val viewModel by viewModels<ModulesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}