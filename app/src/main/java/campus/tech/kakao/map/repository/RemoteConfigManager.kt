package campus.tech.kakao.map.repository

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object RemoteConfigManager {
    private const val REMOTE_SERVICE_STATE = "serviceState"
    private const val REMOTE_SERVICE_MSG = "serviceMessage"
    const val REMOTE_ON_SERVICE = "ON_SERVICE"
    const val REMOTE_ON_ERROR = "ON_ERROR"

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    fun init() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }
    suspend fun fetchAndActivateConfig(): Boolean = suspendCoroutine { continuation ->
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(true)
                } else {
                    Log.e("RemoteConfig", "Fetch failed: ${task.exception}")
                    continuation.resume(false)
                }
            }
    }
    fun getServiceState(): String = remoteConfig.getString(REMOTE_SERVICE_STATE)
    fun getServiceMessage(): String = remoteConfig.getString(REMOTE_SERVICE_MSG)
}