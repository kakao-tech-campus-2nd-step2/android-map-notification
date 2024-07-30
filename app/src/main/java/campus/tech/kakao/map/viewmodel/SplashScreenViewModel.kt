package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> = _serviceMessage

    init {
        setupRemoteConfig()
        fetchRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    private fun fetchRemoteConfig() {
        viewModelScope.launch {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        handleRemoteConfig()
                    } else {
                        Log.d("SplashScreenViewModel", "Fetch failed")
                    }
                }
        }
    }

    private fun handleRemoteConfig() {
        _serviceState.value = remoteConfig.getString("serviceState")
        _serviceMessage.value = remoteConfig.getString("serviceMessage")
    }

}