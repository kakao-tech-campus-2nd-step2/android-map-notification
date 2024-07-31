package campus.tech.kakao.map

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    private val _fetchSuccess = MutableLiveData<Boolean>()
    val fetchSuccess: LiveData<Boolean> get() = _fetchSuccess

    fun fetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val serviceStateValue = remoteConfig.getString("serviceState")
                    val serviceMessageValue = remoteConfig.getString("serviceMessage")

                    Log.d("SplashViewModel", "serviceState: $serviceStateValue")
                    Log.d("SplashViewModel", "serviceMessage: $serviceMessageValue")

                    _serviceState.value = serviceStateValue
                    _serviceMessage.value = serviceMessageValue
                    _fetchSuccess.value = true
                } else {
                    _fetchSuccess.value = false
                }
            }
    }
}
