package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 개발용
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig
            .fetchAndActivate().addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {
                val state = remoteConfig.getString("serviceState")
                if(state == "ON_SERVICE") {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val mainIntent = Intent(this@SplashActivity, MapActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }, 2000)
                }else {
                    Log.d("testtt","state : $state")
                    binding.serviceMessage.isVisible = true
                }
            } else {
                binding.serviceMessage.isVisible = true
                binding.serviceMessage.text = "알수없는 오류가 발생하였습니다."
                Log.d("errorFirebase", "err")
            }
        }
    }
}

