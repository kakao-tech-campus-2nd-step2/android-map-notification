package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this
        binding.viewModel = splashViewModel

        splashViewModel.serviceState.observe(this, Observer { state ->
            if (state == "ON_SERVICE") {
                startActivity(Intent(this, MapActivity::class.java))
                finish()
            } else {
                binding.tvServiceMessage.visibility = TextView.VISIBLE
            }
        })

        splashViewModel.serviceMessage.observe(this, Observer { message ->
            Log.d("SplashActivity", "serviceMessage: $message")
        })
        getFcmToken()
        splashViewModel.handleIntent(intent)
    }
    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SplashActivity", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("SplashActivity", "FCM registration token: $token")
        }
    }
}
