package campus.tech.kakao.map.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository
) : ViewModel() {

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    fun fetchServiceState() {
        splashRepository.getServiceState { serviceState ->
            if (serviceState == "ON_SERVICE") {
                Log.d("yeong", "ViewModel: 통과")
                _navigateToHome.value = true
            } else {
                Log.d("yeong", "ViewModel: 에러")
                _navigateToHome.value = false
                _errorMsg.value = splashRepository.getErrorMsg()
            }
        }
    }
}