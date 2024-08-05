package campus.tech.kakao.map

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class KakaoMapApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initRemoteConfig()
        fetchRemoteConfig()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    private fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val serviceState = remoteConfig.getString("serviceState")
                val serviceMessage = remoteConfig.getString("serviceMessage")
                Log.d("RemoteConfig", "Service State: $serviceState, Service Message: $serviceMessage")
            }
            else {
                val serviceMessage = remoteConfig.getString("serviceMessage")
                val e = task.exception
                Log.e("RemoteConfig", "Fetch and activate failed", e)
            }
        }
    }

    companion object {
        lateinit var remoteConfig: FirebaseRemoteConfig
    }
}