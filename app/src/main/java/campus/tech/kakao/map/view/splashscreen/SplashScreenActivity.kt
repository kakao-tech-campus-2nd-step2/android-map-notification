package campus.tech.kakao.map.view.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.view.kakaomap.KakaoMapActivity
import campus.tech.kakao.map.viewmodel.splashscreen.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        viewModel.getService()
        displaySplashScreen()
    }

    private fun displaySplashScreen() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.serviceState.collect { service ->
                    if (service.state == "ON_SERVICE") {
                        delay(2000)
                        val intent = Intent(this@SplashScreenActivity, KakaoMapActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    binding.service = service
                }
            }
        }
    }
}