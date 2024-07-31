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
    }

    private fun activateRemoteConfig() {
        remoteConfigManager.processRemoteConfig { isSuccess ->
            val setServiceState = remoteConfigManager.getServiceState()

            if (isSuccess) {
                _serviceState.value = setServiceState
                when (setServiceState) {
                    remoteConfigManager.REMOTE_ON_SERVICE -> { _serviceMessage.value = "" }
                    else -> { _serviceMessage.value = remoteConfigManager.getServiceMessage() }
                }
            }
        }
    }
}