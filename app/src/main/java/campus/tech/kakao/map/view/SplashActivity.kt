package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.viewmodel.SplashActivityViewModel
import campus.tech.kakao.map.viewmodel.SplashUIState
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : SplashActivityViewModel by viewModels()
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.splashUiState.collect { uiState ->
                    when(uiState){
                        is SplashUIState.Loading -> {}
                        is SplashUIState.OnService -> {
                            Handler(Looper.getMainLooper()).postDelayed({
                                val mainIntent = Intent(this@SplashActivity, MapActivity::class.java)
                                startActivity(mainIntent)
                                finish()
                            }, 2000)
                        }

                        is SplashUIState.OffService -> {
                            binding.serviceMessage.isVisible = true
                        }
                    }
                }
            }
        }

    }
}

