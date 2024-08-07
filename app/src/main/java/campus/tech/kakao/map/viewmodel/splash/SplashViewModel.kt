package campus.tech.kakao.map.viewmodel.splash

import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.state.ServiceState
import campus.tech.kakao.map.model.state.UiState
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.UnknownServiceException
import javax.inject.Inject

const val FIREBASE_REMOTE_CONFIG_SERVICE_STATE = "serviceState"
const val FIREBASE_REMOTE_CONFIG_SERVICE_MESSAGE = "serviceMessage"

@HiltViewModel
class SplashViewModel @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.NotInitialized)
    val uiState = _uiState.asStateFlow()

    private val _serviceState = MutableStateFlow<ServiceState>(ServiceState.NotInitialized)
    val serviceState = _serviceState.asStateFlow()

    fun startSplash() {
        _uiState.update { UiState.Loading }
    }

    fun finishSplash() {
        if (serviceState.value !is ServiceState.OnService) return
        _uiState.update { UiState.Success }
    }

    private fun onServiceError(e: Exception) {
        _uiState.update { UiState.Error(e) }
    }

    fun initFirebaseRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }

        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            fetchValues()
        }
    }

    private fun FirebaseRemoteConfig.fetchValues() {
        fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val state = getString(FIREBASE_REMOTE_CONFIG_SERVICE_STATE)
                val message = getString(FIREBASE_REMOTE_CONFIG_SERVICE_MESSAGE)
                val serviceState = ServiceState.of(state, message)

                updateServiceState(serviceState)

                if (serviceState !is ServiceState.OnService) {
                    onServiceError(UnknownServiceException(message))
                }
            }
        }
    }

    private fun updateServiceState(serviceState: ServiceState) {
        _serviceState.update { serviceState }
    }
}
