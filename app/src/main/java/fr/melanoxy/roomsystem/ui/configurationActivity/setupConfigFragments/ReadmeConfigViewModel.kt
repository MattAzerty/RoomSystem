package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.module.Module
import fr.melanoxy.roomsystem.data.module.ModuleRepository
import fr.melanoxy.roomsystem.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class ReadmeConfigViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository,
    sharingRepository: SharingRepository,
) : ViewModel() {

    private val moduleId = sharingRepository.currentModuleIdFlow.value
    val singleLiveSelectionConfigEvent = SingleLiveEvent<SelectionConfigEvent>()

    fun getReadmeViewState(): Module {
        return moduleRepository.modulesList[moduleId!!]
    }

    fun onNextButtonClicked() {
        singleLiveSelectionConfigEvent.value = SelectionConfigEvent.LaunchSelectedConfigFragment(moduleId!!)
        }

    }//End of ReadmeConfigViewModel



