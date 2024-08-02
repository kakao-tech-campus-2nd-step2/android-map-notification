package campus.tech.kakao.map

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.utilities.Constants
import campus.tech.kakao.map.view.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("testtt", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("testtt", "Message Notification Body: ${it.body}")
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        sendNotification()
    }

    private fun createNotificationChannel() {
        val descriptionText = "알림 테스트"
        val channel = NotificationChannel(
            Constants.FirebaseMessage.CHANNEL_ID,
            Constants.FirebaseMessage.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(){

        val intent = Intent(this, SplashActivity::class.java).apply { // 클릭하면 출력될 Activity
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(
            this,
            Constants.FirebaseMessage.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.map_app_splash_screen) // 필수
            .setContentTitle("포그라운드 알림")
            .setContentText("앱이 실행 중입니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("포그라운드 발생.")
            )
            .setAutoCancel(true) // 만약 없으면 notification 눌러도 사라지지 않음


        notificationManager.notify(Constants.FirebaseMessage.NOTIFICATION_ID, builder.build())
    }
}