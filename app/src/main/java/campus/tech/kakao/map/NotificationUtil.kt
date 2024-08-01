package campus.tech.kakao.map

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.graphics.BitmapFactory
import campus.tech.kakao.map.view.map.SplashActivity
import campus.tech.kakao.map.view.search.MainActivity

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageTitle: String, messageBody: String, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, SplashActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val mapImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.map_icon2
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        BuildConfig.CHANNEL_ID
    ).setSmallIcon(R.drawable.map_icon2)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setLargeIcon(mapImage)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}