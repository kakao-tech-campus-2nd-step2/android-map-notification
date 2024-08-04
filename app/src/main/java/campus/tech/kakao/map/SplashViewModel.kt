package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {
    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMessage = MutableLiveData<String>()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    fun fetchRemoteConfig(context: Context) {
        RemoteConfig.fetchAndActivate { success ->
            if (success) {
                _serviceState.value = RemoteConfig.getString("serviceState")
                _serviceMessage.value = RemoteConfig.getString("serviceMessage")
            } else {
                _serviceState.value = context.getString(R.string.service_state)
                _serviceMessage.value = context.getString(R.string.service_message)
            }

            Log.d("firebase", serviceMessage.toString())
        }
    }
}