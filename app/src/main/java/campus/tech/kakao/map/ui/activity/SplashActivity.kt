package campus.tech.kakao.map.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import campus.tech.kakao.map.R
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SplashActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var splashText: TextView
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        requestNotificationPermission()

        remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().build())

        val splashImage = findViewById<ImageView>(R.id.splashImage)
        splashText = findViewById(R.id.splashText)

        val rotateAndScale = AnimationUtils.loadAnimation(this, R.anim.rotate_and_scale)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        splashImage.startAnimation(rotateAndScale)

        coroutineScope.launch {
            delay(1000)
            splashText.visibility = View.VISIBLE
            splashText.startAnimation(fadeIn)
        }

        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                remoteConfig.fetchAndActivate().await()
            }

            if (result) {
                val serviceState = remoteConfig.getString("serviceState")
                val serviceMessage = remoteConfig.getString("serviceMessage")

                Log.d("SplashActivity", "serviceState: $serviceState")
                Log.d("SplashActivity", "serviceMessage: $serviceMessage")

                delay(3000)
                if (serviceState == "ON_SERVICE") {
                    val intent = Intent(this@SplashActivity, MapActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    splashText.text = serviceMessage
                }
            } else {
                splashText.text = "서비스를 가져오는 데 실패했습니다."
                Log.d("SplashActivity", "Remote Config fetch failed")
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }
}
