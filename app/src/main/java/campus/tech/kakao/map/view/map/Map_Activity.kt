package campus.tech.kakao.map.view.map

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.R
import campus.tech.kakao.map.service_message
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.kakao.vectormap.MapView

class Map_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapView = MapView(this)
        val mapViewContainer = findViewById<FrameLayout>(R.id.map_view)
        mapViewContainer.addView(mapView)

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // 기본 값 설정
        val defaultValues: Map<String, Any> = mapOf(
            "serviceState" to "ON_SERVICE",
            "serviceMessage" to "서비스가 현재 중지되었습니다."
        )
        remoteConfig.setDefaultsAsync(defaultValues)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this, OnCompleteListener<Boolean> { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    val serviceState: String = remoteConfig.getString("serviceState")
                    val serviceMessage: String = remoteConfig.getString("serviceMessage")

                    // 서비스 상태
                    if ("ON_SERVICE" == serviceState) {
                        // 지도 화면으로 이동
                        startActivity(Intent(this, Map_Activity::class.java))
                        finish()
                    } else {
                        // service_message
                        val intent = Intent(this, service_message::class.java)
                        startActivity(intent)
                    }
                }
            })

        // Firebase message (FCM 사용)
        class MyFirebaseMessagingService : FirebaseMessagingService() {
            override fun onMessageReceived(remoteMessage: RemoteMessage) {
                if (remoteMessage.notification != null) {
                    val title = remoteMessage.notification?.title
                    val message = remoteMessage.notification?.body
                    sendCustomNotification(title, message)
                }
            }

            private fun sendCustomNotification(title: String?, message: String?) {
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "default_channel_id"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Default Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationBuilder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this@Map_Activity, channelId)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)

                notificationManager.notify(0, notificationBuilder.build())
            }
        }
    }
}

