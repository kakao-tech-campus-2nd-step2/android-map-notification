package campus.tech.kakao.map.presentation.view

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.presentation.viewmodel.SplashScreenViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.splash = this

        binding.serviceMessage.visibility = View.GONE

        observeFirebaseValues()

        getFCMToken()
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

    private fun handleServiceState(serviceState: String) {
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

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("FCMToken", msg)
        })
    }
}