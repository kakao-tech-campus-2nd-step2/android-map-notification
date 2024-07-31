package campus.tech.kakao.map.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.LoadingViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Room.MapItemViewModel
import campus.tech.kakao.map.databinding.ActivityLoadingBinding
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {
    private val loadingViewModel: LoadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val binding: ActivityLoadingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_loading)

        loadingViewModel.serviceState.observe(this) {
            Log.d("uin", ""+ loadingViewModel.serviceState.value)
            Log.d("uin", ""+ loadingViewModel.serviceMessage.value)
            if(loadingViewModel.serviceState.value == "ON_SERVICE") {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d("uin", "!!")
                binding.loadingText.text = loadingViewModel.serviceMessage.value
                Log.d("uin", "!!!")
            }
        }

    }
}