package fr.melanoxy.roomsystem.ui.modulesFragment

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
    userRepository: UserRepository,
    sharingRepository: SharingRepository,
    moduleRepository: ModuleRepository
) : ViewModel() {

    val modulesLiveData: LiveData<List<ModuleViewStateItem>> = liveData(Dispatchers.IO) {
        moduleRepository.modulesStateFlow.collect { modules ->
            emit(
                modules.map {
                    ModuleViewStateItem(
                        moduleId = it.moduleId,
                        moduleName = it.moduleName,
                        moduleImageUrl = it.moduleImageUrl,
                        onModuleClicked = {
                            sharingRepository.setCurrentModuleId(it.moduleId)
                        },
                    )
                }
            )
        }
    }

}//END of ModulesViewModel