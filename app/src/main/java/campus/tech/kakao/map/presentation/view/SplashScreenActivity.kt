package campus.tech.kakao.map.presentation.view

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.presentation.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            Log.d("SplashScreenActivity", "Notification permission granted.")
        } else {
            // TODO: Inform user that that your app will not show notifications.
            Log.d("SplashScreenActivity", "Notification permission denied.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.splash = this

        binding.serviceMessage.visibility = View.GONE

        askNotificationPermission()
        observeFirebaseValues()

    }

    private fun observeFirebaseValues() {
        splashScreenViewModel.serviceState.observe(this) { serviceState ->
            handleServiceState(serviceState)
        }

        splashScreenViewModel.serviceMessage.observe(this) { serviceMessage ->
            Log.d("testt", "serviceMessage: $serviceMessage")
            binding.serviceMessage.text = serviceMessage
        }
    }

    private fun handleServiceState(serviceState: String) {
        if (serviceState == "ON_SERVICE") {
            Log.d("testt", "state: $serviceState")
            val intent = Intent(this, KakaoMapViewActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("testt", "state: $serviceState")
            binding.serviceMessage.visibility = View.VISIBLE
        }
    }

    private fun askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // FCM SDK (and your app) can post notifications.
            Log.d("SplashScreenActivity", "Notification permission already granted.")
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
            // 권한 요청 이유를 설명하는 UI를 표시
            showNotificationPermissionDialog()
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this@SplashScreenActivity).apply {
            setTitle(getString(R.string.ask_notification_permission_dialog_title))
            setMessage(
                String.format(
                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(설정에서 %s의 알림 권한을 허용해주세요.",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.deny_notification_permission)) { _, _ -> }
            show()
        }
    }
}