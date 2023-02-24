package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.module.ModuleRepository
import fr.melanoxy.roomsystem.ui.mainActivity.MainEvent
import fr.melanoxy.roomsystem.ui.utils.CoroutineDispatcherProvider
import fr.melanoxy.roomsystem.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectionConfigViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository,
    private val sharingRepository: SharingRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val moduleClickedFlow = sharingRepository.currentModuleIdFlow
    val singleLiveMainEvent = SingleLiveEvent<SelectionConfigEvent>()

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            moduleClickedFlow.collect { moduleNumber ->
                withContext(coroutineDispatcherProvider.main) {
                    if(moduleNumber!=0){
                        singleLiveMainEvent.value = SelectionConfigEvent.LaunchReadmeConfigFragment
                    }
                        }
            }
        }
    }
    

    fun getAllConfigsPossible(): List<SelectionStateItem> {
        return moduleRepository.modulesList.map{
            SelectionStateItem(
                selectionId = it.moduleId,
                selectionField = "${it.moduleId} - ${it.moduleDescription}",
                onSelectionClicked = {
                    sharingRepository.setCurrentModuleId(it.moduleId)
                },
            )
        }.filter { it.selectionId!=0 }
    }





}//END of SelectionConfigViewModel