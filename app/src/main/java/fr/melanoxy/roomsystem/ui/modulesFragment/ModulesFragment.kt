package fr.melanoxy.roomsystem.ui.modulesFragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
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
// The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.top_app_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        changeActionBarTitle()
        bindRecyclerView()



    }

    private fun bindRecyclerView() {

        val adapter = ModulesAdapter()
        binding.moduleFragmentRv.adapter = adapter
        binding.moduleFragmentRv.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.modulesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun changeActionBarTitle() {
        // Access the activity's ActionBar
        val actionBar = (activity as AppCompatActivity).supportActionBar
        // Set the title of the ActionBar
        actionBar?.title = "Modules:"
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_memory_white_24dp)
        }
    }

}