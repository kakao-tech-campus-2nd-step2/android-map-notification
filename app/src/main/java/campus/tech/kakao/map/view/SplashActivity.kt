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
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.viewmodel.SearchActivityViewModel
import campus.tech.kakao.map.viewmodel.SplashActivityViewModel
import campus.tech.kakao.map.viewmodel.SplashUIState
import campus.tech.kakao.map.viewmodel.UiState
import com.google.firebase.Firebase
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
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserve()

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

    

}

