package campus.tech.kakao.map.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityWelcomeBinding
import campus.tech.kakao.map.viewmodel.WelcomeViewModel

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val welcomeViewModel: WelcomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.viewModel = welcomeViewModel
        binding.lifecycleOwner = this
    }
}