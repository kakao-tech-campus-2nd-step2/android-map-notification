package campus.tech.kakao.map.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import campus.tech.kakao.map.data.repository.DefaultFirebaseRemoteConfigRepository
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.ui.map.MapActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
    }

    /**
     * ViewModel의 상태를 관찰하고 UI를 업데이트하는 함수.
     */
    private fun setupObservers() {
        collectRemoteConfig()
        collectErrorMessage()
    }

    /**
     * Remote Config 데이터를 관찰하는 함수.
     */
    private fun collectRemoteConfig() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.remoteConfig.collect { remoteConfigDomain ->
                    if (remoteConfigDomain != null) {
                        handleRemoteConfig(remoteConfigDomain.serviceState, remoteConfigDomain.serviceMessage)
                    }
                }
            }
        }
    }

    /**
     * 에러 메시지를 관찰하는 함수.
     */
    private fun collectErrorMessage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.errorMessage.collect { errorMessage ->
                    errorMessage?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Remote Config 데이터를 처리하는 함수.
     */
    private fun handleRemoteConfig(serviceState: String?, serviceMessage: String?) {
        if (serviceState == DefaultFirebaseRemoteConfigRepository.ON_SERVICE) {
            navigateToMapActivity()
        } else {
            binding.splashMessageTextView.text = serviceMessage
        }
    }

    /**
     * MapActivity로 이동하는 함수.
     */
    private fun navigateToMapActivity() {
        lifecycleScope.launch {
            delay(600)
            val intent = Intent(this@SplashActivity, MapActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
