package campus.tech.kakao.map.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityWelcomeBinding
import campus.tech.kakao.map.data.remote.config.RemoteConfigManager
import campus.tech.kakao.map.presentation.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val welcomeViewModel: WelcomeViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK와 앱이 알림을 게시할 수 있게 된다
        } else {
            Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        observeServiceStateChanges()
        askNotificationPermission()
        setForegroundNotification()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.viewModel = welcomeViewModel
        binding.lifecycleOwner = this
    }
    private fun observeServiceStateChanges(){
        welcomeViewModel.serviceState.observe(this){ state ->
            when(state) {
                RemoteConfigManager.REMOTE_ON_SERVICE -> {
                    Log.d("arieum", state)
                    lifecycleScope.launch {
                        delayBeforeMoveMapView()
                        runOnUiThread { moveMapView() }
                    }
                }
                else -> { Log.d("arieum", state) }
            }
        }
    }
    private suspend fun delayBeforeMoveMapView(){ delay(1000L) }
    private fun moveMapView() {
        val intent = Intent(this@WelcomeActivity, MapViewActivity::class.java)
        startActivity(intent)
    }
    private fun setForegroundNotification(){
        welcomeViewModel.createNotificationChannel(this)
        welcomeViewModel.sendForegroundNotification(this)
    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // FCM SDK와 앱이 이미 알림을 게시할 수 있다
                    // 알림 관련 기능을 초기화하거나 필요한 서비스를 시작한다
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // 사용자에게 권한 요청 이유를 설명하는 UI
                    AlertDialog.Builder(this)
                        .setTitle("알림권한이 필요합니다")
                        .setMessage("This app needs notification permission to send you updates and important information")
                        .setPositiveButton(R.string.positive_button) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton(R.string.negative_button) { dialog, _ ->
                            dialog.dismiss()
                            Toast.makeText(this, "알림권한이 거절됐습니다", Toast.LENGTH_LONG).show()
                        }
                        .show()
                }
                else -> {
                    // 직접 권한요청
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}