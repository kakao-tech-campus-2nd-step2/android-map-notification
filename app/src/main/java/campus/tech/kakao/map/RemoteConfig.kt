package campus.tech.kakao.map

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig

object RemoteConfig {
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 개발용
            }
            setConfigSettingsAsync(configSettings)
        }
    }

    fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                    Log.d("firebase", "fetch successful")
                } else {
                    onComplete(false)
                    Log.d("firebase", "fetch failed")
                }
            }
    }

    fun getString(key: String): String {
        return remoteConfig.getString(key)
    }
}
