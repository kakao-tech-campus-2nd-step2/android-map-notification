package campus.tech.kakao.map.utils

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class UtilityManager @Inject constructor(private val firebaseMessaging: FirebaseMessaging){
    private fun getToken(){
        firebaseMessaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(Constants.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.w(Constants.TAG, "FCM Token: ${task.result}")
        })
    }
}