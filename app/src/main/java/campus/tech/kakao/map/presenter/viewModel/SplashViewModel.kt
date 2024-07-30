package campus.tech.kakao.map.presenter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.ConfigRepository
import campus.tech.kakao.map.domain.vo.Config
import campus.tech.kakao.map.domain.vo.ServiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val configRepository: ConfigRepository
) : ViewModel() {
    private val _config: MutableLiveData<Config> = MutableLiveData(
        Config(serviceState = ServiceState.LOADING, serviceMessage = "Loading...")
    )
    val config: LiveData<Config> = _config

    init {
        viewModelScope.launch {
            _config.postValue(configRepository.getConfig())
        }
    }
}