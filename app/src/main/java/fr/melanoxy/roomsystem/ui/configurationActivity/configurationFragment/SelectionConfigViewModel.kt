package fr.melanoxy.roomsystem.ui.configurationActivity.configurationFragment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.module.ModuleRepository
import fr.melanoxy.roomsystem.ui.mainActivity.modulesFragment.ModuleViewStateItem
import javax.inject.Inject

@HiltViewModel
class SelectionConfigViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository,
    private val sharingRepository: SharingRepository
) : ViewModel() {
    fun getAllConfigsPossible(): List<SelectionStateItem>? {
        return moduleRepository.modulesList.map{
            SelectionStateItem(
                selectionId = it.moduleId,
                selectionField = "${it.moduleId} - ${it.moduleDescription}",
                onSelectionClicked = {
                    println("todo")
                },
            )
        }.filter { it.selectionId!=0 }
    }
}