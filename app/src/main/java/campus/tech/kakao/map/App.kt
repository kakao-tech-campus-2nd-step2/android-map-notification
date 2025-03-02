package campus.tech.kakao.map

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import campus.tech.kakao.map.model.datasource.SharedPreferences
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        super.onCreate()
    }
}
