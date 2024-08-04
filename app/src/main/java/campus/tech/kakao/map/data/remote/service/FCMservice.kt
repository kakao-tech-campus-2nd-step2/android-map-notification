package campus.tech.kakao.map.data.remote.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presentation.view.WelcomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMservice : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("TEST_FcmService", "Received Message")

        remoteMessage.notification?.let { notification ->
            // 일반 알림 처리
            showNotification(notification.title, notification.body)
        }

        remoteMessage.data.let { data ->
            // 커스텀 알림 처리
            val title = data["title"]
            val body = data["body"]
            if (title != null && body != null) {
                showNotification(title, body)
            }
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext,
            REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channelId = CHANNEL_ID
        val channelName = CHANNEL_NAME
        val channelDescription = CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 222222
        private const val REQUEST_CODE = 0
        private const val CHANNEL_ID = "Mychannel"
        private const val CHANNEL_NAME = "Mychannel"
        private const val CHANNEL_DESCRIPTION = "This is Mychannel"
    }
}