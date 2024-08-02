package campus.tech.kakao.map.view

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityEnterBinding
import campus.tech.kakao.map.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterBinding
    private val viewModel: MyViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (및 앱)가 알림을 게시할 수 있습니다.
        } else {
            // 사용자에게 앱이 알림을 표시하지 않을 것임을 알립니다.
            Toast.makeText(this, "알림 권한 거부", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase 초기화 (필요시)
        // FirebaseApp.initializeApp(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter)
        binding.viewModel = viewModel

        viewModel.fetchRemoteConfig()

        askNotificationPermission()

        viewModel.remoteConfigState.observe(this, Observer {
            if (it) {
                Log.d("seyoung", "intent : $it")
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
            }
        })

        viewModel.remoteConfigMessage.observe(this, Observer {
            binding.enterText.text = it
        })
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // 이미 권한이 허용된 경우
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 설명하는 UI를 표시
                showNotificationPermissionDialog()
            } else {
                // 직접 권한 요청
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        val message = String.format(
            "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림에서 %s의 알림 권한을 허용해주세요.)",
            getString(R.string.app_name)
        )

        AlertDialog.Builder(this).apply {
            setTitle("알림 권한 요청")
            setMessage(message)
            setPositiveButton("예") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton("거부") { _, _ -> }
            show()
        }
    }
}
