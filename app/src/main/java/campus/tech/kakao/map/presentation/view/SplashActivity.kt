package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()
    }

    private fun observeViewModel() {

        splashViewModel.navigationEvent.observe(this) {event ->
            if (event) {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        splashViewModel.serviceMessage.observe(this) { message ->
            binding.serviceMessageTextView.text = message
        }
    }
    }
