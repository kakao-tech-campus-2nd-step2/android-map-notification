package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.MainActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import campus.tech.kakao.map.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val serviceState = firebaseRemoteConfig.getBoolean("serviceState")
                    val serviceMessage = firebaseRemoteConfig.getString("serviceMessage")

                    if (serviceState) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {

                        Log.e("SplashActivity", "Service is unavailable: $serviceMessage")
                    }
                } else {
                    Log.e("SplashActivity", "Failed to fetch remote config")
                }
            }
    }
}
