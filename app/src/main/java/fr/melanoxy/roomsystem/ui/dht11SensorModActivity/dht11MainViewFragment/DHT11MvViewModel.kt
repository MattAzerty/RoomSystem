package fr.melanoxy.roomsystem.ui.dht11SensorModActivity.dht11MainViewFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.melanoxy.roomsystem.data.module.ModuleRepository
import javax.inject.Inject

@HiltViewModel
class DHT11MvViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository,
) : ViewModel() {

    val realTimeTemperatureLiveData = moduleRepository.getRealTimeTemperatureFlow().asLiveData()

    fun setIsUserConnected(isUserConnected: Boolean) {
        moduleRepository.setOnRTDBIsUserConnected(isUserConnected)
    }

}