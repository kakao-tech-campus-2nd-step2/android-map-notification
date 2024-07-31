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
     val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
     val serviceMessage: LiveData<String> get() = _serviceMessage


    private val _navigationEvent = MutableLiveData<Boolean>()
     val navigationEvent: LiveData<Boolean> get() = _navigationEvent


    val DEFAULT_STRING = "️"

    init {
        fetchRemoteConfig()
    }

    fun fetchRemoteConfig() {
        viewModelScope.launch {
            // 스플래시 화면 확인을 위해 넣은 딜레이
            delay(2000)
            runBlocking {
                getRemoteConfigUseCase("serviceState").observeForever { state ->
                    _serviceState.value = state
                    if (state != "ON_SERVICE") {
                        runBlocking {
                            getRemoteConfigUseCase("serviceMessage").observeForever { message ->
                                _serviceMessage.value = message
                                _navigationEvent.value = false
                            }
                        }
                    } else {
                        _serviceMessage.value = DEFAULT_STRING
                        _navigationEvent.value = true
                    }
                }
            }
        }
    }


}