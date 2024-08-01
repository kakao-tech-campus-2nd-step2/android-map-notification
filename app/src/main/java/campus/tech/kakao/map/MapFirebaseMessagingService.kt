package campus.tech.kakao.map

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: ${message.from}")

        message.notification?.let {
            sendNotification("[foreground]" + (it.title ?: ""), it.body ?: "")
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notificationChannelName),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.notificationChannelDescription)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun sendNotification(title: String, message: String) {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.map_notification_icon)
            .setColor(getColor(R.color.notificationBackground))
            .setContentTitle(title)
            .setContentText(message)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    companion object {
        private const val TAG = "MapFirebaseMessagingService"
        private const val CHANNEL_ID = "custom_notification_channel_id"
    }
}