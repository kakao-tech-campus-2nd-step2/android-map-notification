package campus.tech.kakao.map.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.domain.usecase.GetRemoteConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getRemoteConfigUseCase: GetRemoteConfigUseCase
) : ViewModel() {
    private val _serviceState = MutableLiveData<String>()
    val serviceState: MutableLiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: MutableLiveData<String> get() = _serviceMessage

    // serviceState 값의 변화로 화면 이동을 하니 스플래시 스크린 확인이 어려워서 추가함
    private val _navigationEvent = MutableLiveData<Boolean>()
    val navigationEvent: LiveData<Boolean> get() = _navigationEvent

    val DEFAULT_STRING = "️"

    init {
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        viewModelScope.launch {
            // 스플래시 화면 확인을 위해 넣은 딜레이
            delay(2000)
            getRemoteConfigUseCase("serviceState").observeForever { state ->
                _serviceState.value = state
                if (state != "ON_SERVICE") {
                    getRemoteConfigUseCase("serviceMessage").observeForever { message ->
                        _serviceMessage.value = message
                    }
                } else {
                    _serviceMessage.value = DEFAULT_STRING
                }
            }
        }
    }


}