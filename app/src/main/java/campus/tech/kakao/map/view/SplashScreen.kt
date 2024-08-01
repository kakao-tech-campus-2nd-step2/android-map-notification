package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
	var serviceMessage:String = ""
	private lateinit var splashScreenBinding: ActivitySplashScreenBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
		splashScreenBinding.splash = this
		installSplashScreen()
		setContentView(splashScreenBinding.root)
		getRemoteConfig()
	}

	private fun moveMap(sec: Int) {
		lifecycleScope.launch {
			delay((sec * 1000).toLong())
			startActivity(Intent(this@SplashScreen, MapActivity::class.java))
			finish()
		}
	}

	private fun getRemoteConfig(){
		val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
		val configSettings = remoteConfigSettings {
			minimumFetchIntervalInSeconds = 0
		}
		remoteConfig.setConfigSettingsAsync(configSettings)
		remoteConfig.fetchAndActivate()
			.addOnCompleteListener(this) { task ->
				val state = remoteConfig.getString(getString(R.string.serviceState))
				if (task.isSuccessful && state == getString(R.string.serviceState_default)) {
					moveMap(1)
				} else {
					serviceMessage = remoteConfig.getString(getString(R.string.serviceMessage))
					splashScreenBinding.invalidateAll()
				}
			}
	}
}