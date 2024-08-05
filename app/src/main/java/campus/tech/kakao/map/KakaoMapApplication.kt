package campus.tech.kakao.map

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
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
        createNotificationChannel()
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
                val e = task.exception
                Log.e("RemoteConfig", "Fetch and activate failed", e)
            }
        }
    }

    private fun createNotificationChannel() {
        val descriptionText = getString(R.string.channel_description)
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        lateinit var remoteConfig: FirebaseRemoteConfig
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }
}