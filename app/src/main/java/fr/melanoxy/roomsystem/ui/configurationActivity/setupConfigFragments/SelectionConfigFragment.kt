package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.ConfigurationListFragmentBinding
import fr.melanoxy.roomsystem.ui.mainActivity.modulesFragment.ModulesFragment
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class SelectionConfigFragment : Fragment(R.layout.configuration_list_fragment) {
    private val binding by viewBinding { ConfigurationListFragmentBinding.bind(it) }
    private val viewModel by viewModels<SelectionConfigViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRecyclerView()
        //Event(s) observer
        viewModel.singleLiveMainEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                SelectionConfigEvent.LaunchReadmeConfigFragment -> switchToReadmeFragment()
                else -> {//TODO: something went wrong
                 }
            }
        }
    }

    private fun switchToReadmeFragment() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_configuration_FrameLayout_container, ReadmeConfigFragment())
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    private fun bindRecyclerView() {

        val adapter = SelectionAdapter()
        binding.configListFragmentRecyclerView.adapter = adapter
        adapter.submitList(viewModel.getAllConfigsPossible())
    }
}//END of SelectionConfigFragment