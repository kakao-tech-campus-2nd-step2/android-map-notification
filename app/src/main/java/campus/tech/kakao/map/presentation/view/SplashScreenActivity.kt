package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.presentation.viewmodel.SplashScreenViewModel

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.splash = this

        binding.serviceMessage.visibility = View.GONE

        observeFirebaseValues()
    }

    private fun observeFirebaseValues() {
        splashScreenViewModel.serviceState.observe(this) { serviceState ->
            handleServiceState(serviceState)
        }

        splashScreenViewModel.serviceMessage.observe(this) { serviceMessage ->
            Log.d("testt", "serviceMessage: $serviceMessage")
            binding.serviceMessage.text = serviceMessage
        }
    }

    private fun handleServiceState(serviceState: String){
        if (serviceState == "ON_SERVICE") {
            Log.d("testt", "state: $serviceState")
            val intent = Intent(this, KakaoMapViewActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("testt", "state: $serviceState")
            binding.serviceMessage.visibility = View.VISIBLE
        }
    }
}