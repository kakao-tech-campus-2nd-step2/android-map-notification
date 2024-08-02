package campus.tech.kakao.map


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService: FirebaseMessagingService() {
	private val notification = Notification()
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		remoteMessage.notification?.let {remoteMessageContent->
			notification.createNotification(remoteMessageContent.title, remoteMessageContent.body, this)
		}
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
	}

}