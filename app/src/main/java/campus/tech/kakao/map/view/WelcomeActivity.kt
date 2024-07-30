package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityWelcomeBinding
import campus.tech.kakao.map.repository.RemoteConfigManager
import campus.tech.kakao.map.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val welcomeViewModel: WelcomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        observeServiceStateChanges()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.viewModel = welcomeViewModel
        binding.lifecycleOwner = this
    }
    private fun observeServiceStateChanges(){
        welcomeViewModel.serviceState.observe(this){ state ->
            when(state) {
                RemoteConfigManager.REMOTE_ON_SERVICE -> {
                    lifecycleScope.launch {
                        binding.serverMsg.text = ""
                        delayBeforeMoveMapView()
                        runOnUiThread { moveMapView() }
                    }
                }
                else -> {
                    binding.serverMsg.text = welcomeViewModel.getCurrentServiceMsgConfigValues()
                }
            }
        }
    }
    private suspend fun delayBeforeMoveMapView(){ delay(2000L) }
    private fun moveMapView() {
        val intent = Intent(this@WelcomeActivity, MapViewActivity::class.java)
        startActivity(intent)
    }
}