package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.SplashLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: SplashLayoutBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.splash_layout)

        splashViewModel.serviceState.observe(this) { serviceState ->
            if (serviceState == "ON_SERVICE") {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
            else {
                splashViewModel.serviceMessage.observe(this) { serviceMessage ->
                    splashBinding.tvError.text = serviceMessage
                }
            }
        }
    }
}