package campus.tech.kakao.map


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _serviceState: MutableLiveData<String> = MutableLiveData()
    val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMessage: MutableLiveData<String> = MutableLiveData()
    val serviceMessage: LiveData<String> get() = _serviceMessage

    init{
        //updateRemoteConfig()
        updateServiceState()
        updateServiceMessage()
    }

    fun updateServiceState() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    //getServiceState()
                    //getServiceMessage()
                    _serviceState.postValue(remoteConfig.getString("serviceState"))
                }
            }
    }

    fun updateServiceMessage() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    _serviceMessage.postValue(remoteConfig.getString("serviceMessage"))
                }
            }
    }

    private fun getServiceState() {
        _serviceState.postValue(remoteConfig.getString("serviceState"))
    }

    private fun getServiceMessage() {
        _serviceMessage.postValue(remoteConfig.getString("serviceMessage"))
    }

}