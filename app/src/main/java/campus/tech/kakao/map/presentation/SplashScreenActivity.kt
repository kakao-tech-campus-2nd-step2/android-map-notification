package campus.tech.kakao.map.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.databinding.ActivitySplashScreenBinding
import campus.tech.kakao.map.presentation.map.MapActivity
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUI()
        setRemoteConfig()
    }

    private fun setupUI() {
        initBinding()

        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    private fun setRemoteConfig(){
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            fetchValues()
        }
    }
    private fun FirebaseRemoteConfig.fetchValues() {
        fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                handleRemoteConfigSuccess()
            }else{
                handleRemoteConfigFail(it.exception?.message)
            }
        }
    }

    private fun handleRemoteConfigSuccess(){
        val state = Firebase.remoteConfig.getString("serviceState")
        val message = Firebase.remoteConfig.getString("serviceMessage")

        if(state == "ON_SERVICE"){
            startMapActivity()
        }else{
            setSplashScreenMessage(message)
        }
    }

    private fun handleRemoteConfigFail(errorMessage: String?){
        if(!PlaceApplication.isNetworkActive()){
            startMapActivity()
        }else{
            setSplashScreenMessage(errorMessage ?: "연결에 문제가 발생했습니다.")
        }
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.lifecycleOwner = this
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