package campus.tech.kakao.map.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityInitBinding
import campus.tech.kakao.map.domain.model.RemoteConfig
import campus.tech.kakao.map.presentation.viewmodel.InitViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitActivity : AppCompatActivity() {
    private val TAG = "sumin"
    private val viewModel: InitViewModel by viewModels()
    private lateinit var binding: ActivityInitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_init)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w(TAG,"Fetching FCM registration token failed", it.exception)
                return@addOnCompleteListener
            }
            val token = it.result
            Log.d(TAG, token)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.updateRemoteConfig()
        viewModel.remoteConfig.observe(this) {
            startServiceIfAvailable(it)
        }
    }

    private fun startServiceIfAvailable(remoteConfig: RemoteConfig) {
        if (isServiceAvailable(remoteConfig)) {
            val mapIntent = Intent(this, MapActivity::class.java)
            startActivity(mapIntent)
            finish()
        } else {
            binding.serviceMessage.text = remoteConfig.serviceMessage
        }
    }

    private fun isServiceAvailable(remoteConfig: RemoteConfig): Boolean = remoteConfig.serviceState == "ON_SERVICE"
}