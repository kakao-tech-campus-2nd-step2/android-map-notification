package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) : ViewModel() {

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    init {
        configureRemoteConfig()
        getRemoteConfig()
    }

    private fun configureRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(
            SERVICE_STATE_KEY to OFF_SERVICE,
            SERVICE_MESSAGE_KEY to DEFAULT_SERVICE_MESSAGE
        ))
    }

    private fun getRemoteConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            val state = remoteConfig.getString("serviceState")
            val message = remoteConfig.getString("serviceMessage")

            when (state) {
                ON_SERVICE -> _navigateToMain.value = true
                else -> _serviceMessage.value = message
            }
        }
    }

    companion object {
        const val SERVICE_STATE_KEY = "serviceState"
        const val SERVICE_MESSAGE_KEY = "serviceMessage"
        const val OFF_SERVICE = "OFF_SERVICE"
        const val ON_SERVICE = "ON_SERVICE"
        const val DEFAULT_SERVICE_MESSAGE = "서버 점검 중 입니다. 나중에 다시 시도해 주세요."
    }



}

