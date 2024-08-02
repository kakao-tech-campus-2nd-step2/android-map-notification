package campus.tech.kakao.map

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import campus.tech.kakao.map.BuildConfig
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KakaoMapApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
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
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }
}