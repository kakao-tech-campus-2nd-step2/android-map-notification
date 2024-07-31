package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.RemoteConfig
import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.repository.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemoteConfigViewModel @Inject constructor(
    remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val _remoteConfigLiveData = MutableLiveData<RemoteConfig>()
    val remoteConfigLiveData: LiveData<RemoteConfig> get() = _remoteConfigLiveData

    init {
        _remoteConfigLiveData.value = remoteConfigRepository.getRemoteConfig()
    }
    fun isOnService(): Boolean {
        return _remoteConfigLiveData.value?.serviceState.equals("ON_SERVICE")
    }

    fun getServiceMessage(): String{
        return _remoteConfigLiveData.value?.serviceMessage.toString()
    }


}