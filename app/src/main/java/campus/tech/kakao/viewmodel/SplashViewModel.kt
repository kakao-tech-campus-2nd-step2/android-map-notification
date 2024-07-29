package campus.tech.kakao.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class SplashViewModel : ViewModel() {

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    val serviceMessage = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()

    init {
        _navigateToMain.value = false
        imageUrl.value = "@drawable/splash"
    }

    fun fetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val serviceState = remoteConfig.getString("serviceState")
                    val serviceMessage = remoteConfig.getString("serviceMessage")
                    Log.d("1234", "fetchRemoteConfig: $serviceState $serviceMessage")
                    updateConfig(serviceState, serviceMessage)
                } else {
                    updateConfig(
                        remoteConfig.getString("serviceState"),
                        remoteConfig.getString("serviceMessage")
                    )
                }
            }
    }

    private fun updateConfig(serviceState: String, serviceMessage: String) {
        if (serviceState == "ON_SERVICE") {
            Handler(Looper.getMainLooper()).postDelayed({
                _navigateToMain.value = true
            }, 2000)
        } else {
            this.serviceMessage.value = serviceMessage
            Handler(Looper.getMainLooper()).postDelayed({
                _navigateToMain.value = false
            }, 2000)
        }
    }
}