package campus.tech.kakao.map.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ui.activity.SplashActivity
import com.google.firebase.messaging.RemoteMessage

class NotificationService(val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun notifyFromFCM(message: RemoteMessage.Notification) {
        notifyMessage(message.title, message.body)
    }

    private fun notifyMessage(title: String?, content:String?) {
        Log.d("fcm", "$title $content")

        val intent = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(
            context,
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


    private fun createNotificationChannel() {
        Log.d("notify", "실행")
        val descriptionText = FM_CHANNEL_DESCRIPTION
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }


    companion object {
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
        private const val FM_CHANNEL_DESCRIPTION = "main channelDescription"
    }

}