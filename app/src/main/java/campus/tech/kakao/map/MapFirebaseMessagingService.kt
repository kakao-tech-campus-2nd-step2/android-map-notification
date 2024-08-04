package campus.tech.kakao.map

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MapFirebaseMessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var notificationHelper: AppStatusNotificationHelper

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            // 백그라운드 상태에서 FCM 기본 알림 발생
            Log.d(TAG, "Message Notification Body: ${remoteMessage.notification!!.body}")
        }

        // 앱이 포그라운드 상태일 경우 커스텀 알림 발생
        if (remoteMessage.data.isNotEmpty()) {
            handleCustomNotification()
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, ""+token)
        // 새로운 토큰을 서버에 저장
    }

    private fun handleCustomNotification() {
        notificationHelper.showNotification()
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}