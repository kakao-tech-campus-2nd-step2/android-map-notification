package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        splashBinding.viewModel = splashViewModel
        splashBinding.lifecycleOwner = this

        splashViewModel.serviceState.observe(this, Observer { serviceState ->
            handleServiceState(serviceState)
        })

        splashViewModel.fetchRemoteConfig(this)
    }

    private fun handleServiceState(serviceState: String) {
        if (serviceState == "ON_SERVICE") {
            lifecycleScope.launch {
                delay(2000)
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // 뒤로가기 방지!
            }
        }
    }
}