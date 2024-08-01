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
                _serviceState.value = getRemoteConfigUseCase("serviceState")
                if (_serviceState.value != "ON_SERVICE") {
                    _serviceMessage.value = getRemoteConfigUseCase("serviceMessage")
                    _navigationEvent.value = false
                } else {
                        _serviceMessage.value = DEFAULT_STRING
                        _navigationEvent.value = true
                }
        }
    }
}