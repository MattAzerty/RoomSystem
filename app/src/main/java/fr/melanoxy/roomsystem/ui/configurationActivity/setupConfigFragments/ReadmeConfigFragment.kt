package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.melanoxy.roomsystem.R
import fr.melanoxy.roomsystem.databinding.ReadmeConfigFragmentBinding
import fr.melanoxy.roomsystem.ui.configurationActivity.selectedConfigFragments.sensorDHT11.ESP32DHT11ConfigFragment
import fr.melanoxy.roomsystem.ui.utils.viewBinding

@AndroidEntryPoint
class ReadmeConfigFragment : Fragment(R.layout.readme_config_fragment) {

    private val binding by viewBinding { ReadmeConfigFragmentBinding.bind(it) }
    private val viewModel by viewModels<ReadmeConfigViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.readmeConfigRequirementTv.text = viewModel.getReadmeViewState().moduleRequirement
        binding.readmeConfigStepsTv.text = viewModel.getReadmeViewState().moduleSteps

        binding.readmeConfigNextButton.setOnClickListener { viewModel.onNextButtonClicked() }

        //Event(s) observer
        viewModel.singleLiveSelectionConfigEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SelectionConfigEvent.LaunchSelectedConfigFragment -> switchSelectedConfigurationFragment(event.moduleSelected)
                //SelectionConfigEvent.LaunchReadmeConfigFragment ->
                else -> {//TODO: something went wrong
                }
            }
        }
    }

    private fun switchSelectedConfigurationFragment(moduleSelected: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_configuration_FrameLayout_container, selectedFragment(moduleSelected))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun selectedFragment(moduleSelected: Int): Fragment {
            return when(moduleSelected){
                1 -> ESP32DHT11ConfigFragment()
                else -> ReadmeConfigFragment()
            }
    }

}//END of ReadmeConfig