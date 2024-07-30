package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.RemoteConfigManager
import dagger.hilt.android.lifecycle.HiltViewModel
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
        activateRemoteConfig()
        updateCurrentServiceStateConfigValues()
    }

    // 앱 첫 설치시 한번만 실행
    private fun activateRemoteConfig() {
        remoteConfigManager.processRemoteConfig { isSuccess ->
            val setServiceState = remoteConfigManager.getServiceState()
            val setServiceMessage = remoteConfigManager.getServiceMessage()
            if (isSuccess) {
                _serviceState.value = setServiceState
                when (setServiceState) {
                    remoteConfigManager.REMOTE_ON_SERVICE -> {}
                    else -> { _serviceMessage.value = setServiceMessage }
                }
            }
        }
    }

    // 앱 설치 이후
    private fun updateCurrentServiceStateConfigValues() {
        _serviceState.value = remoteConfigManager.getServiceState()
    }

    fun getCurrentServiceMsgConfigValues(): String = remoteConfigManager.getServiceMessage()
}