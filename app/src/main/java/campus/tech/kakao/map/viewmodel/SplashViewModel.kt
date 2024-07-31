package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    private val _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean> get() = _navigateToMap

    init {
        loadRemoteConfig()
    }

    fun loadRemoteConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                val serviceState = remoteConfig.getString("serviceState")
                val serviceMessage = remoteConfig.getString("serviceMessage")
                if (serviceState == "ON_SERVICE") {
                    _navigateToMap.value = true
                } else {
                    _serviceMessage.value = serviceMessage
                    _navigateToMap.value = false
                }
            }
    }
}
