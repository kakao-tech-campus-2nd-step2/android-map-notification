package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.LoadingLayoutBinding
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class LoadingActivity : AppCompatActivity() {
    private lateinit var loadingBinding: LoadingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_layout)
        loadingBinding = DataBindingUtil.setContentView(this, R.layout.loading_layout)

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 개발용
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) {
                val serviceState = remoteConfig.getString("serviceState")
                if (serviceState == "ON_SERVICE") {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                }
                else {
                    val serviceMessage = remoteConfig.getString("serviceMessage")
                    loadingBinding.tvError.text = serviceMessage
                }
            }


    }
}