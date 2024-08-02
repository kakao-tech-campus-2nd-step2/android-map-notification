package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityEnterBinding
import campus.tech.kakao.map.viewmodel.MyViewModel
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterBinding
    private val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter)
        binding.viewModel = viewModel

        viewModel.fetchRemoteConfig()

        viewModel.remoteConfigState.observe(this, Observer {
            if(it){
                Log.d("seyoung","intent : $it")
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        viewModel.remoteConfigMessage.observe(this, Observer {
            binding.enterText.setText(it)
        })


    }
}