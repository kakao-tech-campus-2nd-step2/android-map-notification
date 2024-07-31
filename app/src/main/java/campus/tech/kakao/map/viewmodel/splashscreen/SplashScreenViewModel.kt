package campus.tech.kakao.map.viewmodel.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.splashscreen.Service
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

    private val _service = MutableStateFlow<Service>(Service("", ""))
    val service: StateFlow<Service> get() = _service

    fun getService() {
        viewModelScope.launch {
            _service.value = firebaseRepository.getService()
        }
    }
}