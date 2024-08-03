package campus.tech.kakao.map

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseApp
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapApplication: Application() {
//    companion object {
//        lateinit var prefs: PreferenceManager
//    }

    override fun onCreate() {
        val apiKey = getString(R.string.kakao_api_key)
//        prefs = PreferenceManager(applicationContext)
        super.onCreate()
        KakaoMapSdk.init(this, apiKey)
//        FirebaseApp.initializeApp(this)
        try {
            FirebaseApp.initializeApp(this)
            Log.d("MapApplication", "FirebaseApp initialized successfully")
        } catch (e: Exception) {
            Log.e("MapApplication", "FirebaseApp initialization failed", e)
        }

    }
}