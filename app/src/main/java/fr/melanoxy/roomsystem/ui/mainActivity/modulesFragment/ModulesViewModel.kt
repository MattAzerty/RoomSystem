package fr.melanoxy.roomsystem.ui.mainActivity.modulesFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.activityCrossFragment.SharingRepository
import fr.melanoxy.roomsystem.data.module.ModuleRepository
import fr.melanoxy.roomsystem.data.user.UserRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ModulesViewModel @Inject constructor(
    sharingRepository: SharingRepository,
    moduleRepository: ModuleRepository
) : ViewModel() {

    val modulesLiveData: LiveData<List<ModuleViewStateItem>> = liveData(Dispatchers.IO) {
        moduleRepository.getUserModules().collect { modulesId ->
            emit(
                modulesId.map {
                    ModuleViewStateItem(
                        moduleId = it,
                        moduleName = moduleRepository.modulesList[it].moduleName,
                        moduleImageUrl = moduleRepository.modulesList[it].moduleImageUrl,
                        onModuleClicked = {
                            sharingRepository.setCurrentModuleId(it)
                        },
                    )
                }.asReversed()//.filter {it.moduleId==0 || it.moduleId==2 }
            )
        }
    }

}//END of ModulesViewModel