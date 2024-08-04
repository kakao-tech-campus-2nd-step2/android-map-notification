package campus.tech.kakao.map.data.source

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.presentation.InitActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val kakaoTechChannel = getChannel(KAKAO_TECH_ID, KAKAO_TECH_NAME, KAKAO_TECH_DESCRIPTION, NotificationManager.IMPORTANCE_DEFAULT)

        val intent = Intent(this, InitActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, KAKAO_TECH, intent, PendingIntent.FLAG_IMMUTABLE)
        val kakaoTechNotification = getNotification(message, KAKAO_TECH_ID, pendingIntent)

        notificationManager.createNotificationChannel(kakaoTechChannel)
        notificationManager.notify(KAKAO_TECH, kakaoTechNotification)
    }

    private fun getChannel(id: String, name: String, description: String = "", importance: Int): NotificationChannel {
        return NotificationChannel(id, name, importance).apply {
            this.description = description
            enableLights(true)
            enableVibration(true)
            vibrationPattern = longArrayOf(100L, 200L, 300L)
        }
    }

    private fun getNotification(message: RemoteMessage, id: String, pendingIntent: PendingIntent): Notification {
        return NotificationCompat.Builder(this, id)
            .setSmallIcon(android.R.drawable.ic_secure)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["text"])
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        const val KAKAO_TECH = 0
        const val KAKAO_TECH_ID = "kakaoTech"
        const val KAKAO_TECH_NAME = "카카오테크 캠퍼스 알림"
        const val KAKAO_TECH_DESCRIPTION = "카카오테크 캠퍼스 관련 알림입니다"
    }
}