package campus.tech.kakao.map

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MapFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("uin", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("uin", "Message Notification Body: ${it.body}")
        }
    }

}