package campus.tech.kakao.map.view.map

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.service_message
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kakao.vectormap.MapView


class Map_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(campus.tech.kakao.map.R.layout.activity_map)

        val mapView = MapView(this)
        val mapViewContainer = findViewById<FrameLayout>(campus.tech.kakao.map.R.id.map_view)
        mapViewContainer.addView(mapView)
        val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings: FirebaseRemoteConfigSettings = Builder()
            .setMinimumFetchIntervalInSeconds(3600) // 1시간마다 새 값을 가져옴
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)


// 기본 값 설정
        val defaultValues: MutableMap<String, Any> = HashMap()
        defaultValues["serviceState"] = "OFF_SERVICE"
        defaultValues["serviceMessage"] = "서비스가 현재 중지되었습니다."
        mFirebaseRemoteConfig.setDefaultsAsync(defaultValues)


        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(
                this,
                OnCompleteListener<Boolean> { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        val serviceState: String = mFirebaseRemoteConfig.getString("serviceState")
                        val serviceMessage: String =
                            mFirebaseRemoteConfig.getString("serviceMessage")

                        // 서비스 상태에 따른 로직 처리
                        if ("ON_SERVICE" == serviceState) {
                            // 지도 화면으로 이동
                            startActivity(
                                Intent(
                                    this, Map_Activity::class.java
                                )
                            )
                            finish()
                        } else {
                            // 서비스 메시지를 화면에 표시
                            val intent = Intent(this, service_message::class.java)
                            startActivity(intent)
                        }
                    }
                })

    }
}
