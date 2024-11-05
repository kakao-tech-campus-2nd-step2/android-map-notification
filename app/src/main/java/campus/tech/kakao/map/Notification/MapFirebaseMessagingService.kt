package campus.tech.kakao.map.Notification


import campus.tech.kakao.map.Notification.NotificationManager.createNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService: FirebaseMessagingService() {
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		remoteMessage.notification?.let {remoteMessageContent->
			createNotification(remoteMessageContent.title, remoteMessageContent.body, this)
		}
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
	}

}