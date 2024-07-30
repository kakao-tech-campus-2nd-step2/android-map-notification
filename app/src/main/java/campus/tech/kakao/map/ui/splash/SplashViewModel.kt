package campus.tech.kakao.map.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.FirebaseRemoteConfigDomain
import campus.tech.kakao.map.domain.usecase.FetchRemoteConfigUseCase
import campus.tech.kakao.map.domain.usecase.GetRemoteConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fetchRemoteConfigUseCase: FetchRemoteConfigUseCase,
    private val getRemoteConfigUseCase: GetRemoteConfigUseCase,
) : ViewModel() {

    private val _remoteConfig = MutableStateFlow<FirebaseRemoteConfigDomain?>(null)
    val remoteConfig: StateFlow<FirebaseRemoteConfigDomain?> get() = _remoteConfig

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        viewModelScope.launch {
            try {
                val isSuccess = fetchRemoteConfigUseCase()
                if (isSuccess) {
                    getRemoteConfig()
                } else {
                    _errorMessage.value = "remote config fetch가 실패하였습니다."
                }
            } catch (e: Exception) {
                _errorMessage.value = "remote config fetch가 실패하였습니다."
            }
        }
    }

    private fun getRemoteConfig() {
        viewModelScope.launch {
            try {
                val remoteConfig = getRemoteConfigUseCase()
                _remoteConfig.value = remoteConfig
            } catch (e: Exception) {
                _errorMessage.value = "remote config 정보를 가져 오지 못했습니다."
            }
        }
    }
}
