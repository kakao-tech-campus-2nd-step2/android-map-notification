package campus.tech.kakao.map.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
	var serviceMessage:String = ""
	private val requestPermissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestPermission(),
	) { isGranted: Boolean ->
		if (isGranted) {
			getRemoteConfig()
		} else {
			getRemoteConfig()
		}
	}
	private lateinit var splashScreenBinding: ActivitySplashScreenBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
		splashScreenBinding.splash = this
		installSplashScreen()
		setContentView(splashScreenBinding.root)
		askNotificationPermission()
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



	private fun askNotificationPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (ContextCompat.checkSelfPermission(
					this,
					android.Manifest.permission.POST_NOTIFICATIONS
				) ==
				PackageManager.PERMISSION_GRANTED
			) {
				getRemoteConfig()
			} else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
				showNotificationPermissionDialog()
			} else {
				requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)

			}
		}
	}

	private fun showNotificationPermissionDialog() {
		AlertDialog.Builder(this@SplashScreen).apply {
			setTitle(getString(R.string.ask_notification_permission_dialog_title))
			setMessage(
				String.format(
					"다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(설정에서 %s의 알림 권한을 허용해주세요.)",
					getString(R.string.app_name)
				)
			)
			setPositiveButton(getString(R.string.yes)) { _, _ ->
				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
				val uri = Uri.fromParts("package", packageName, null)
				intent.data = uri
				startActivity(intent)
				getRemoteConfig()
			}
			setNegativeButton(getString(R.string.deny_notification_permission)) { _, _ ->
				getRemoteConfig()
			}
			show()
		}
	}

	override fun onRestart() {
		getRemoteConfig()
		super.onRestart()
	}

	private fun getFcmToken(){
		FirebaseMessaging.getInstance().token.addOnCompleteListener(
			OnCompleteListener {
				if(!it.isSuccessful){
					return@OnCompleteListener
				}
				val token = it.result
				Log.d("testt", token.toString())
			}
		)
	}



}