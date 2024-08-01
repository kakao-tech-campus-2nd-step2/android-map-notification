package campus.tech.kakao.map.activity

import android.Manifest
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.LoadingViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityLoadingBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {
    private val loadingViewModel: LoadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val binding: ActivityLoadingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_loading)

        loadingViewModel.serviceState.observe(this) {
            if (loadingViewModel.serviceState.value == "ON_SERVICE") {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                binding.loadingText.text = loadingViewModel.serviceMessage.value
            }
        }
    }
}
