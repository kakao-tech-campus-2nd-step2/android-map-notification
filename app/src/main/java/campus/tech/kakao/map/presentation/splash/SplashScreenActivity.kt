package campus.tech.kakao.map.presentation.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.PlaceApplication.Companion.isNetworkActive
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.presentation.splash.RemoteConfigState.*
import campus.tech.kakao.map.presentation.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setupUI()
        observeViewModel()
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.lifecycleOwner = this
    }

    private fun setupUI() {
        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    private fun observeViewModel(){
        viewModel.remoteConfigState.observe(this){ state ->
            when(state){
                is Loading -> Log.d("splash_state", "loading")
                is Success -> handleServiceState(state.data.serviceState, state.data.serviceMessage)
                is Failure -> handleErrorState(state.errorMessage)
            }
        }
    }

    private fun handleServiceState(serviceState: String?, serviceMessage: String?) {
        if (serviceState == "ON_SERVICE") {
            startMapActivity()
        } else {
            val message = serviceMessage ?: ""
            setSplashScreenMessage(message)
        }
    }

    private fun handleErrorState(errorMessage: String?){
        val message = errorMessage ?: "연결에 문제가 발생했습니다"

        if (!isNetworkActive(this)){
            startMapActivity()
        }else{
            setSplashScreenMessage(message)
        }
    }

    private fun setSplashScreenMessage(message: String){
        binding.tvSplashMessage.text = message
    }

    private fun startMapActivity(){
        val intent = Intent(this@SplashScreenActivity, MapActivity::class.java)
        startActivity(intent)
        finish()
    }
}