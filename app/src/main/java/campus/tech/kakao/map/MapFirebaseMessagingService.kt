package campus.tech.kakao.map


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import campus.tech.kakao.map.view.SplashScreen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService : FirebaseMessagingService() {
	private lateinit var notificationManager: NotificationManager
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		remoteMessage.notification?.let {remoteMessageContent->
			createNotification(remoteMessageContent.title, remoteMessageContent.body)
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


	private fun createNotification(title: String?, body: String?){
		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		createNotificationChannel()
		val intent = Intent(this, SplashScreen::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		}
		val pendingIntent = PendingIntent.getActivity(
			this,
			0,
			intent,
			PendingIntent.FLAG_IMMUTABLE
		)
		val builder = NotificationCompat.Builder(
			this,
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
	}

}