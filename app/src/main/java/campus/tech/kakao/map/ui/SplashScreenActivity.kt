package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.serviceState.observe(this) { state ->
            if (state == "ON_SERVICE") {
                navigateToMapActivity()
            } else {
                showServiceMessage()
            }
        }
    }

    private fun navigateToMapActivity() {
        Log.d("SplashScreen", "Success")
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }, 3000)
    }

    private fun showServiceMessage() {
        Log.d("SplashScreen", "Failed")
        binding.tvServiceMessage.text = viewModel.serviceMessage.value
    }
}