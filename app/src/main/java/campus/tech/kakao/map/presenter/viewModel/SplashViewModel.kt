package campus.tech.kakao.map.presenter.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.ConfigRepository
import campus.tech.kakao.map.domain.vo.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val configRepository: ConfigRepository
) : ViewModel() {
    private val _config : MutableLiveData<Config> = MutableLiveData()
    val config : LiveData<Config> = _config

    init{
        _config.value = configRepository.getConfig()
    }
}