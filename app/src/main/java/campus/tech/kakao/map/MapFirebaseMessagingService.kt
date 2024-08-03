package campus.tech.kakao.map

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MapFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("firebase", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("firebase", "Message Notification Body: ${it.body}")
        }
    }
}