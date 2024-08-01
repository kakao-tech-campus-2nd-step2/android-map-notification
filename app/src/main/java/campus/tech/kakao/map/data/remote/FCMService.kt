package campus.tech.kakao.map.data.remote

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService: FirebaseMessagingService() {

    val TAG = "FCM"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "From: ${message.from}")
        val notificationService = NotificationService(this)
        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.title}")
            Log.d(TAG, "Message Notification Body: ${it.body}")
            notificationService.notifyFromFCM(it)
        }

    }
}