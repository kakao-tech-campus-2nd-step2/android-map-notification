package campus.tech.kakao.map.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> = _serviceMessage

    init {
        remoteConfigValues()
    }

    fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val data: Uri? = intent.data
            data?.let {
                // Process the URI if needed
            }
        }
    }

    private fun remoteConfigValues() {
        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _serviceState.value = remoteConfig.getString("serviceState")
                _serviceMessage.value = remoteConfig.getString("serviceMessage")
            } else {
                _serviceMessage.value = "Failed to fetch remote config values"
            }
        }
    }
}