package campus.tech.kakao.map.data.remote.network

import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(@NonNull remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    } // 수신 메시지 처리

    override fun onNewToken(@NonNull token: String) {
        Log.d("testt", "Refreshed token: $token")
    } // 토큰 갱신
}