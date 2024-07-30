package campus.tech.kakao.map.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.dto.PlaceVO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _serviceState = MutableLiveData<String>()
    val serviceState: MutableLiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: MutableLiveData<String> get() = _serviceMessage

}