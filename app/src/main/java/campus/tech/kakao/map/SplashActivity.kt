package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.view.MapActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // 개발용
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        viewModel.fetchRemoteConfig(remoteConfig)

        viewModel.fetchSuccess.observe(this, Observer { success ->
            if (success) {
                if (viewModel.serviceState.value == "ON_SERVICE") {
                    startActivity(Intent(this, MapActivity::class.java))
                    finish()
                } else {
                    Log.d("SplashActivity", "Observed serviceMessage: ${viewModel.serviceMessage.value}")
                    binding.serviceMessageTextView.text = viewModel.serviceMessage.value
                }
            }
        })
    }
}
