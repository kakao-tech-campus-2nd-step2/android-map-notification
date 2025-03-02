package campus.tech.kakao.map.view.map

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.view.search.MainActivity
import campus.tech.kakao.map.viewmodel.RemoteConfigViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var activitySplashBinding: ActivitySplashBinding
    private val remoteConfigViewModel: RemoteConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding.root)

        setupSplashScreen()
    }

    private fun setupSplashScreen() {
        val isOnService = remoteConfigViewModel.isOnService()
        Log.d("jieun", "isOnService: " + isOnService)
        if (isOnService) {
            Handler(Looper.getMainLooper()).postDelayed({
                // 일정 시간이 지나면 MapActivity로 이동
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()

            }, 1000)
        } else {
            activitySplashBinding.serverErrorMessage.visibility = View.VISIBLE
            activitySplashBinding.serverErrorMessage.text =
                remoteConfigViewModel.getServiceMessage()
        }
    }
}