package campus.tech.kakao.map.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.viewmodel.SearchActivityViewModel
import campus.tech.kakao.map.viewmodel.SplashActivityViewModel
import campus.tech.kakao.map.viewmodel.SplashUIState
import campus.tech.kakao.map.viewmodel.UiState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserve()

        getFCMToken()


    }

    fun initObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.splashUiState.collect { uiState ->
                    Log.d("testtt", uiState.toString())
                    UpdateSplash(uiState)
                }
            }
        }
    }

    fun UpdateSplash(uiState : SplashUIState){
        when (uiState) {
            is SplashUIState.Loading -> {
                binding.serviceMessage.isVisible = true
                binding.serviceMessage.text = "로딩"
            }

            is SplashUIState.OnService -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    val mainIntent =
                        Intent(this@SplashActivity, MapActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }, 2000)
            }

            is SplashUIState.OffService -> {
                binding.serviceMessage.isVisible = true
                binding.serviceMessage.text = uiState.message
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "notification이 전송됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "notification이 전송되지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO 사용자에게 버튼 제공
                showNotificationPermissionDialog()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this@SplashActivity).apply {
            setTitle("권한 확인")
            setMessage(
                String.format(
                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림 에서 %s의 알림 권한을 허용해주세요.)",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton("yes") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton("no") { _, _ -> }
            show()
        }
    }

    fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            Log.d("testtt", task.result.toString())

        })
    }


}

