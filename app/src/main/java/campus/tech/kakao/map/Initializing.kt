package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Initializing : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
    }
}