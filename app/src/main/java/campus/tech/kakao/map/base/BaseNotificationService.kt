package campus.tech.kakao.map.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presenter.service.FcmService
import campus.tech.kakao.map.presenter.view.SplashActivity

class BaseNotificationService(private val context: Context) {
    private var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun notifyFromFCM(message: String){
        notify("Firebase Cloud Message","Firebase로 부터 온 알림입니다.",message)
    }

    fun notifyFromBroadcastReceiver(message: String){
        notify("Broadcast Receiver","Broadcast Receiver로 부터 온 알림입니다.",message)
    }


    private fun notify(title: String, content : String, message:String) {
        notificationManager.createNotificationChannel(createNotificationChannel())
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
            .setSmallIcon(R.drawable.place_image)
            .setContentTitle("[카테캠 Step2] $title")
            .setContentText("포그라운드 상태의 알림입니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$content : $message")
            )
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
        private const val CHANNEL_DESCRIPTION = "main channelDescription"

    }
}