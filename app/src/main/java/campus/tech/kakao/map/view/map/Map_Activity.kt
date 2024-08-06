package campus.tech.kakao.map.view.map

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.service_message
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kakao.vectormap.MapView


class Map_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(campus.tech.kakao.map.R.layout.activity_map)

        val mapView = MapView(this)
        val mapViewContainer = findViewById<FrameLayout>(campus.tech.kakao.map.R.id.map_view)
        mapViewContainer.addView(mapView)
        val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings: FirebaseRemoteConfigSettings = Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1시간마다 새 값을 가져옴
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)


// 기본 값 설정
        val defaultValues: MutableMap<String, Any> = HashMap()
        defaultValues["serviceState"] = "OFF_SERVICE"
        defaultValues["serviceMessage"] = "서비스가 현재 중지되었습니다."
        mFirebaseRemoteConfig.setDefaultsAsync(defaultValues)


        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(
                this,
                OnCompleteListener<Boolean> { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        val serviceState: String = mFirebaseRemoteConfig.getString("serviceState")
                        val serviceMessage: String =
                            mFirebaseRemoteConfig.getString("serviceMessage")

                        // 서비스 상태
                        if ("ON_SERVICE" == serviceState) {
                            // 지도 화면으로 이동
                            startActivity(
                                Intent(
                                    this, Map_Activity::class.java
                                )
                            )
                            finish()
                        } else {
                            // service_message
                            val intent = Intent(this, service_message::class.java)
                            startActivity(intent)
                        }
                    }
                })

        //firebase message(FCM 사용)
        class MyFirebaseMessagingService : FirebaseMessagingService() {
            override fun onMessageReceived(remoteMessage: RemoteMessage) {
                // 메시지가 포그라운드 상태일 때 수신될 경우
                if (remoteMessage.notification != null) {
                    val title = remoteMessage.notification!!.title
                    val message = remoteMessage.notification!!.body
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
                    NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)

                notificationManager.notify(0, notificationBuilder.build())
            }
        }


    }
}
