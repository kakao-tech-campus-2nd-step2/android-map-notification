package campus.tech.kakao.map.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.ui.map.MapActivity
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRemoteConfig()
        fetchRemoteConfig()
    }

    /**
     * Firebase Remote Config 설정 초기화
     */
    private fun setupRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 개발용
            // minimumFetchIntervalInSeconds = 3600 // 실제 사용시
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    /**
     * Firebase Remote Config 데이터를 Fetch하고 활성화 함수.
     */
    private fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    handleRemoteConfig()
                } else {
                    showFetchFailedMessage()
                }
            }
    }

    /**
     * Remote Config 데이터를 처리하는 함수.
     */
    private fun handleRemoteConfig() {
        val serviceState = remoteConfig.getString(SERVICE_STATE)
        Log.d("RemoteConfig", "state: $serviceState")
        if (serviceState == ON_SERVICE) {
            navigateToMapActivity()
        } else {
            val serviceMessage = remoteConfig.getString(SERVICE_MESSAGE)
            binding.splashMessageTextView.text = serviceMessage
        }
    }

    /**
     * Fetch 실패 시 메시지 표시.
     */
    private fun showFetchFailedMessage() {
        Toast.makeText(
            this,
            "Fetch failed",
            Toast.LENGTH_SHORT,
        ).show()
    }

    /**
     * MapActivity로 이동하는 함수
     */
    private fun navigateToMapActivity() {
        lifecycleScope.launch {
            delay(600)
            val intent = Intent(this@SplashActivity, MapActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object ConfigKeys {
        const val SERVICE_STATE = "serviceState"
        const val ON_SERVICE = "ON_SERVICE"
        const val SERVICE_MESSAGE = "serviceMessage"
    }
}
