package campus.tech.kakao.map.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.usecase.GetServiceInformationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val getServiceInformationUseCase: GetServiceInformationUseCase
) : ViewModel() {

    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> = _serviceMessage

    init {
        viewModelScope.launch {
            val serviceInformation = getServiceInformationUseCase()
            _serviceState.postValue(serviceInformation.serviceState)
            _serviceMessage.postValue(serviceInformation.serviceMessage)
        }
    }
}