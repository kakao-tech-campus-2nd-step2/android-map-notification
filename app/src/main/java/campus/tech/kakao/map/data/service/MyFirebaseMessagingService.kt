package campus.tech.kakao.map.data.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presentation.splash.SplashScreenActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("myToken", "Token: ${token}")
    }

    // Foreground 에서 호출 됨
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, message: String?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        val pendingIntent = setPendingIntent()
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_location)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true) //사용자가 알림을 클릭하면 해당 알림이 자동으로 사라짐

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun setPendingIntent(): PendingIntent{

        val intent = Intent(this, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE // 생성 후 수정 x
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "기본 채널"
        }
        notificationManager.createNotificationChannel(channel)
    }
}
