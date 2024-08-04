package campus.tech.kakao.map

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.FirebaseApp
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MapApplication: Application(), LifecycleObserver {

    @Inject
    lateinit var notificationHelper: AppStatusNotificationHelper

    override fun onCreate() {

        val apiKey = getString(R.string.kakao_api_key)
        super.onCreate()
        KakaoMapSdk.init(this, apiKey)
        try {
            FirebaseApp.initializeApp(this)
            Log.d("MapApplication", "FirebaseApp initialized successfully")
        } catch (e: Exception) {
            Log.e("MapApplication", "FirebaseApp initialization failed", e)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        notificationHelper.showNotification()
    }
}