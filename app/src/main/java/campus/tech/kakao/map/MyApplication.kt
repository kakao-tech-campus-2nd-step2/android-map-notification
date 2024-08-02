package campus.tech.kakao.map

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import campus.tech.kakao.map.presenter.SystemBroadcastReceiver
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        KakaoMapSdk.init(this,BuildConfig.KAKAO_API_KEY)
        registerReceiver()
        super.onCreate()
    }

    private fun registerReceiver(){
        val filter = IntentFilter().apply{
            this.addAction(Intent.ACTION_BATTERY_CHANGED)
            this.addAction(Intent.ACTION_BOOT_COMPLETED)
            this.addAction(Intent.ACTION_SCREEN_ON)
        }
        ContextCompat.registerReceiver(
            this,
            SystemBroadcastReceiver(),
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful) Log.d("myTag",it.result)
        }
    }
}