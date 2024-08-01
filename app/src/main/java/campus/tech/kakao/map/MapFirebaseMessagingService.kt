package campus.tech.kakao.map

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: ${message.from}")

        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    companion object {
        private const val TAG = "MapFirebaseMessagingService"
    }
}