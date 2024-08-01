package campus.tech.kakao.map.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.ui.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: SplashViewModel
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.serviceState.observe(this) { serviceState ->
            if (serviceState == "ON_SERVICE") {
                val intent = Intent(this@SplashActivity, MapActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.splashInformationTextView.isVisible = true
            }
        }
    }
}