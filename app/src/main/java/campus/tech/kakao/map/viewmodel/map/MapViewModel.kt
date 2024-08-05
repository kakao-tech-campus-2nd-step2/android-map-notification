package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.state.UiState
import campus.tech.kakao.map.viewmodel.RemoteConfigManager
import campus.tech.kakao.map.repository.map.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    remoteConfigManager: RemoteConfigManager
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.NotInitialized)
    val uiState: LiveData<UiState> get() = _uiState

    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    init {
        _serviceState.value = remoteConfigManager.serviceState
        _serviceMessage.value = remoteConfigManager.serviceMessage
    }

    // Map 관련 메소드
    fun showErrorView(e: Exception) {
        _uiState.value = UiState.Error(e)
    }

    fun showSuccessView() {
        _uiState.value = UiState.Success
    }

    fun updateLastPosition(latitude: Double, longitude: Double) {
        mapRepository.updateLastPosition(latitude, longitude)
    }

    fun readLastPosition(): Pair<Double?, Double?> {
        return mapRepository.readLastPosition()
    }
}
