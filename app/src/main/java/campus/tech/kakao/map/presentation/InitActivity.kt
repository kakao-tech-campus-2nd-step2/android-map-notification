package campus.tech.kakao.map.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityInitBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class InitActivity : AppCompatActivity() {
    private val TAG = "InitActivity"
    private lateinit var binding: ActivityInitBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityInitBinding>(this, R.layout.activity_init)
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    if (isServiceAvailable(remoteConfig)) {
                        val mapIntent = Intent(this, MapActivity::class.java)
                        startActivity(mapIntent)
                    } else {
                        binding.serviceMessage.text = getServiceMessage(remoteConfig)
                    }
                }
            }
    }
    private fun isServiceAvailable(remoteConfig: FirebaseRemoteConfig): Boolean = getServiceState(remoteConfig) == "ON_SERVICE"
    private fun getServiceState(remoteConfig: FirebaseRemoteConfig): String = remoteConfig.getString("serviceState")
    private fun getServiceMessage(remoteConfig: FirebaseRemoteConfig): String = remoteConfig.getString("serviceMessage")
}