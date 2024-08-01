package campus.tech.kakao.map.service

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import campus.tech.kakao.map.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("jieun", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("jieun", "Message Notification: ${it.title} ${it.body}")
            sendNotification(it.title!!, it.body!!)
        }
    }

    private fun sendNotification(messageTitle: String, messageBody: String) {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(messageTitle, messageBody, applicationContext)
    }
}