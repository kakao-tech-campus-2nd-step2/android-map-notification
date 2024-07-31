package campus.tech.kakao.map.repository

import campus.tech.kakao.map.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigManager {
    private const val REMOTE_SERVICE_STATE = "serviceState"
    private const val REMOTE_SERVICE_MSG = "serviceMessage"
    const val REMOTE_ON_SERVICE = "ON_SERVICE"

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    fun init() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }
    fun processRemoteConfig(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    onComplete(result)
                } else {
                    onComplete(false)
                }
            }
    }
    fun getServiceState(): String = remoteConfig.getString(REMOTE_SERVICE_STATE)
    fun getServiceMessage(): String = remoteConfig.getString(REMOTE_SERVICE_MSG)
}