package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.repository.RemoteConfigManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager,
): ViewModel() {
    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    init {
        processRemoteConfig()
    }

    private fun processRemoteConfig() {
        viewModelScope.launch {
            val success = remoteConfigManager.fetchAndActivateConfig()
            if (success) {
                val setServiceState = remoteConfigManager.getServiceState()
                _serviceState.value = setServiceState
                when (setServiceState) {
                    RemoteConfigManager.REMOTE_ON_SERVICE -> { _serviceMessage.value = "" }
                    else -> { _serviceMessage.value = remoteConfigManager.getServiceMessage() }
                }
            } else {
                _serviceState.value = remoteConfigManager.REMOTE_ON_ERROR
                _serviceMessage.value = "Failed to load..."
            }
        }
    }
}