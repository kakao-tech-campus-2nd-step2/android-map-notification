package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.KakaoMapApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.SplashLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: SplashLayoutBinding
    private val splashDisplayLength: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.splash_layout)

        val remoteConfig = KakaoMapApplication.remoteConfig
        val serviceState = remoteConfig.getString("serviceState")
        val serviceMessage = remoteConfig.getString("serviceMessage")

        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDisplayLength)
            if (serviceState == "ON_SERVICE") {
                val intent = Intent(this@SplashActivity, MapActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                splashBinding.tvError.text = serviceMessage
            }
        }
    }
}