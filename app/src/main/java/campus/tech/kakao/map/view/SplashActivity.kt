package campus.tech.kakao.map.view

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setBinding()
        setViewModel()

        askNotificationPermission()
        //getFcmToken()
    }

    /*private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("yeong","token: ${it.result}")
                return@addOnCompleteListener
            }
        }
    }*/


    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    private fun setViewModel() {
        binding.lifecycleOwner = this
        binding.viewModel = splashViewModel
    }

    private fun startAppFlow() {
        splashViewModel.fetchServiceState()
        setScreen()
    }

    private fun setScreen() {
        splashViewModel.navigateToHome.observe(this, Observer { navigateToHome ->
            if (navigateToHome) {
                navigateToHomeMapActivity()
            }
        })
    }

    private fun navigateToHomeMapActivity() {
        val intent = Intent(this, HomeMapActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            startAppFlow()
        } else {
            showNotificationPermissionDialog()
        }
    }

    private fun askNotificationPermission() {
        // 33 (TIRAMISU) 버전 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                startAppFlow()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showNotificationPermissionDialog()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this@SplashActivity).apply {
            setTitle(getString(R.string.ask_notification_permission_dialog_title))
            setMessage(
                String.format(
                    "\n권한을 허용하시면, 다양한 알림 소식을 받으실 수 있습니다.\n(알림에서 %s의 알림 권한을 허용해주세요.)",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton(getString(R.string.accept_notification_permission)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.deny_notification_permission)) { _, _ ->
                handleAppExit()
            }
            setCancelable(true)

            setOnCancelListener {
                handleAppExit()
            }
            show()
        }
    }

    private fun handleAppExit() {
        Toast.makeText(
            this@SplashActivity, "앱을 사용하기 위해 알림 권한이 필요합니다. 권한을 허용해주세요!",
            Toast.LENGTH_SHORT
        ).show()
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 1000)
    }
}