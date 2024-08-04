package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import campus.tech.kakao.map.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.serviceState.observe(this) { state ->
            handleServiceState(state)
        }
        viewModel.serviceMessage.observe(this) { message ->
            handleServiceMessage(message)
        }

        viewModel.fetchRemoteConfig()
    }

    private fun handleServiceState(state: String) {
        if (state == "ON_SERVICE") {
            navigateToMainActivity()
        }
    }

    private fun handleServiceMessage(message: String) {
        findViewById<TextView>(R.id.splash_message).text = message
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
