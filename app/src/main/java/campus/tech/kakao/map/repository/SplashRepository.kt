package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.data.firebase.RemoteConfig
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val remoteConfig: RemoteConfig
) {
    fun getServiceState(onComplete: (String?) -> Unit) {
        remoteConfig.fetchAndActivate { isActivated ->
            if (isActivated) {
                val state = remoteConfig.getData(SERVICE_STATE_KEY)
                Log.d("yeong", "Repository: state = $state")
                onComplete(state)
            } else {
                Log.d("yeong", "Repository: 호출 오류")
                onComplete(null)
            }
        }
    }

    fun getErrorMsg(): String {
        return remoteConfig.getData(SERVICE_MESSAGE_KEY)
    }

    companion object {
        const val SERVICE_STATE_KEY = "serviceState"
        const val SERVICE_MESSAGE_KEY = "serviceMessage"
    }
}