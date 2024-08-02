package campus.tech.kakao.map.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.MapActivity
import campus.tech.kakao.map.ui.SplashScreenActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MapFCM", "Refreshed token: $token")
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("remoteMessage", "From: ${message.from}")

        message.notification?.let {
            Log.d("remoteMessage", "Message Notification Body: ${it.body}")

            sendNotification()
        }
    }

    private fun createNotificationChannel() {
        val descriptionText = getString(R.string.fcm_channel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification() {
        val intent = Intent(this, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
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

    companion object {
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }

}