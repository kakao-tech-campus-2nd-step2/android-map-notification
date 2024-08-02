package campus.tech.kakao.map.data.remote.network

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import campus.tech.kakao.map.presentation.view.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService : FirebaseMessagingService() {
    companion object {
        const val CHANNEL_ID = "default_notification_channel_id"
        const val CHANNEL_NAME = "Default Channel"
    }

    override fun onMessageReceived(@NonNull remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("testt", "From: ${remoteMessage.notification?.title}")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            remoteMessage.notification?.let {
                showNotification(it)
            }
        } else {
            Log.d("testt", "Notification permission not granted.")
        }
    }

    override fun onNewToken(@NonNull token: String) {
        Log.d("testt", "Refreshed token: $token")
    } // 토큰 갱신

    private fun showNotification(notification: RemoteMessage.Notification) {
        try {
            val intent = Intent(this, SplashActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val mario = BitmapFactory.decodeResource(resources, campus.tech.kakao.map.R.drawable.mario)
            val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationColor = ContextCompat.getColor(this, R.color.holo_red_light)

            // 커스텀 설정
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(campus.tech.kakao.map.R.drawable.mario)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(notificationColor)
                .setLargeIcon(mario)

            val notificationManager = NotificationManagerCompat.from(this)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0, notificationBuilder.build())
        } catch (e: SecurityException) {
            // 권한 부족에 따른 예외 처리
            Log.e("testt", "권한 없음: ${e.message}")
        }
    }
}