package campus.tech.kakao.map.data.remote

import android.util.Log
import campus.tech.kakao.map.data.vo.Config
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class ConfigService {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0 // 개발용
    }

    init {
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    suspend fun getConfig(): Config {
        remoteConfig.fetchAndActivate().await()
        Log.d("config", Thread.currentThread().name)
        return Config(
            remoteConfig.getString(SERVICE_STATE),
            remoteConfig.getString(SERVICE_MESSAGE)
        )
    }

    companion object {
        private const val SERVICE_STATE = "serviceState"
        private const val SERVICE_MESSAGE = "serviceMessage"
    }

    object ServiceState {
        const val ON_SERVICE = "ON_SERVICE"
    }

}