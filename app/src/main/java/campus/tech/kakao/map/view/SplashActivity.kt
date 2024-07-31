package campus.tech.kakao.map.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.navigateToMap.observe(this) {
            if (it) {
                Log.d("testt", "d")
                navigateToMap()
            } else {
                Log.d("testt", "y")
            }
        }

        viewModel.serviceMessage.observe(this) {
            binding.message.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            binding.message.text = it
        }
    }

    private fun navigateToMap() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }

}
