package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class SplashActivity : AppCompatActivity() {
    lateinit var tvServiceMessage: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvServiceMessage = findViewById(R.id.tvServiceMessage)

        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        fetchRemoteConfigValues(remoteConfig)
    }

    private fun fetchRemoteConfigValues(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val state = remoteConfig.getString("serviceState")
                Log.d("SplashActivity", "serviceState: $state")

                if (state == "ON_SERVICE") {
                    startActivity(Intent(this, MapActivity::class.java))
                } else {
                    tvServiceMessage.text = "서버 점검 중 입니다. 나중에 다시 시도해 주세요."
                    tvServiceMessage.visibility = TextView.VISIBLE
                }
            } else {
                Toast.makeText(this, "Failed to fetch remote config values", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
