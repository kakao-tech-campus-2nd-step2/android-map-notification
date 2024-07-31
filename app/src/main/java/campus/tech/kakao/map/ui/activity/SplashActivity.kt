package campus.tech.kakao.map.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import campus.tech.kakao.map.R

class SplashActivity : AppCompatActivity() {
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var splashText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        val splashImage = findViewById<ImageView>(R.id.splashImage)
        splashText = findViewById(R.id.splashText)

        val rotateAndScale = AnimationUtils.loadAnimation(this, R.anim.rotate_and_scale)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        splashImage.startAnimation(rotateAndScale)

        Handler().postDelayed({
            splashText.visibility = View.VISIBLE
            splashText.startAnimation(fadeIn)
        }, 1000)

        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val serviceState = remoteConfig.getString("serviceState")
                val serviceMessage = remoteConfig.getString("serviceMessage")

                Log.d("SplashActivity", "serviceState: $serviceState")
                Log.d("SplashActivity", "serviceMessage: $serviceMessage")

                Handler().postDelayed({
                    if (serviceState == "ON_SERVICE") {
                        val intent = Intent(this@SplashActivity, MapActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        splashText.text = serviceMessage
                    }
                }, 3000)
            } else {
                splashText.text = "서비스를 가져오는 데 실패했습니다."
                Log.d("SplashActivity", "Remote Config fetch failed")
            }
        }
    }
}
