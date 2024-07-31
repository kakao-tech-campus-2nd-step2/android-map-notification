package campus.tech.kakao.map.viewmodel.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.splashscreen.ServiceState
import campus.tech.kakao.map.repository.splashscreen.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private var firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _serviceState = MutableStateFlow<ServiceState>(ServiceState("", ""))
    val serviceState: StateFlow<ServiceState> get() = _serviceState

    fun getService() {
        viewModelScope.launch {
            _serviceState.value = firebaseRepository.getService()
        }
    }
}