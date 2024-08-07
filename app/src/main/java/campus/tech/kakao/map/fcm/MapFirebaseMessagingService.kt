package campus.tech.kakao.map.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            createNotificationChannel()

            val intent = Intent(this, SplashActivity::class.java).apply {
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
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("[중요] 포그라운드 알림")
                .setContentText("앱이 실행 중입니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("앱이 실행 중일 때는 포그라운드 알림이 발생합니다.")
                )
                .setAutoCancel(true)

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val descriptionText = "카테캠 맵 알림"
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        private const val NOTIFICATION_ID = 111111
        private const val CHANNEL_ID = "map_default_channel"
        private const val CHANNEL_NAME = "카테캠 알림 채널"
    }
}
