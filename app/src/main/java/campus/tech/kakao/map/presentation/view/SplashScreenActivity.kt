package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.splash = this

        binding.serviceMessage.visibility = View.GONE

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)


        fetchValues()
    }

    private fun fetchValues() {
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val serviceState = Firebase.remoteConfig.getString("serviceState")
                val serviceMessage = Firebase.remoteConfig.getString("serviceMessage")
                Log.d("testt", "state: $serviceState")
                Log.d("testt", "serviceMessage: $serviceMessage")
                if (serviceState == "ON_SERVICE") {
                    val intent = Intent(this, KakaoMapViewActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.serviceMessage.visibility = View.VISIBLE
                    binding.serviceMessage.text = serviceMessage
                }
            }
        }
    }
}