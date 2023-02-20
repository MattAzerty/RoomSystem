package fr.melanoxy.roomsystem.ui.configurationActivity.configurationFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.ConfigurationListFragmentBinding
import fr.melanoxy.roomsystem.ui.mainActivity.modulesFragment.ModulesAdapter
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class SelectionConfigFragment : Fragment(R.layout.configuration_list_fragment) {
    private val binding by viewBinding { ConfigurationListFragmentBinding.bind(it) }
    private val viewModel by viewModels<SelectionConfigViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRecyclerView()

    }

    private fun bindRecyclerView() {

        val adapter = SelectionAdapter()
        binding.configListFragmentRecyclerView.adapter = adapter
        adapter.submitList(viewModel.getAllConfigsPossible())
    }
}//END of SelectionConfigFragment