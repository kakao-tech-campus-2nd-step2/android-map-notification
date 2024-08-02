package campus.tech.kakao.map

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.view.SplashScreen

class Notification {
	private lateinit var notificationManager: NotificationManager
	private fun createNotificationChannel(context: Context) {
		val channel = NotificationChannel(
			CHANNEL_ID,
			CHANNEL_NAME,
			NotificationManager.IMPORTANCE_DEFAULT
		).apply {
			description = CHANNEL_DESCRIPTION
		}
		notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
	}


	fun createNotification(title: String?, body: String?, context: Context){


		createNotificationChannel(context)
		val intent = Intent(context, SplashScreen::class.java).apply {
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
			.setContentTitle("[포그라운드] $title")
			.setContentText("$body")
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
		private const val CHANNEL_DESCRIPTION = "main channelDescription"
	}
}