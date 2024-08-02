package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySplashBinding
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)


        viewModel.fetchRemoteConfig(firebaseRemoteConfig)
        viewModel.toMap.observe(this, Observer { map ->
            if (map) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                binding.messageTextView.text = viewModel.serviceMessage.value
            }
        })
    }
}