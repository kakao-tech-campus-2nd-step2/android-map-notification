package campus.tech.kakao.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    val imageUrl = MutableLiveData<String>().apply {
        value = "@drawable/splash"
    }

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            _navigateToMain.value = true
        }, 2000) // 2초 후에 실행
    }
}