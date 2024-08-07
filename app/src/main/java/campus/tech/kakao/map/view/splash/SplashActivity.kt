package campus.tech.kakao.map.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.model.state.ServiceState
import campus.tech.kakao.map.model.state.UiState
import campus.tech.kakao.map.view.map.MapActivity
import campus.tech.kakao.map.viewmodel.splash.SplashViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        init()

        fcmToken()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.splashActivity = this
        binding.lifecycleOwner = this
    }

    private fun init() {
        splashViewModel.initFirebaseRemoteConfig()

        lifecycleScope.launch {
            splashViewModel.uiState.collectLatest {
                if (it is UiState.Success) {
                    val intent = Intent(this@SplashActivity, MapActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        lifecycleScope.launch {
            splashViewModel.serviceState.collectLatest {
                if (it.isError()) {
                    binding.bottomTextView.visibility = View.VISIBLE
                } else if (it is ServiceState.OnService) {
                    binding.bottomTextView.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            delay(100L)
            splashViewModel.startSplash()
            delay(1400L)
            splashViewModel.finishSplash()
        }
    }

    private fun fcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "SplashActivity",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }
                Log.d("SplashActivity", "FCM Token: ${task.result}")
            }
        )
    }
}
