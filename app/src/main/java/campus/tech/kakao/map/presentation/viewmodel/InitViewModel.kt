package campus.tech.kakao.map.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.RemoteConfig
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {
    private val _remoteConfig = MutableLiveData<RemoteConfig>()
    val remoteConfig: LiveData<RemoteConfig> get() = _remoteConfig

    fun updateRemoteConfig() {
        viewModelScope.launch {
            _remoteConfig.value = remoteConfigRepository.fetchAndActivateRemoteConfig()
        }
    }
}