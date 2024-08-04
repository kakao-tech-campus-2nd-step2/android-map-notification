package campus.tech.kakao.map.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.presentation.viewmodel.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val TAG = "KAKAOMAP"

    private val viewModel : MapViewModel by viewModels()
    private lateinit var binding: ActivityMapBinding
    private lateinit var kakaoMap: KakaoMap
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "알림 권한이 부여되지 않아 알림을 받을 수 없어요.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)

        viewModel.lastLocation.observe(this) {
            it?.let { updateView(it) }
        }
        binding.kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // TODO: 필요시 구현 예정
            }

            override fun onMapError(exception: Exception?) {
                showErrorPage(exception)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                viewModel.updateLastLocation()
                Log.d(TAG, "onMapReady")
            }
        })

        binding.searchBox.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        askNotificationPermission()
    }

    override fun onResume() {
        super.onResume()
        binding.kakaoMapView.resume()
        viewModel.updateLastLocation()
    }

    override fun onPause() {
        super.onPause()
        binding.kakaoMapView.pause()
        Log.d("KAKAOMAP", "onPause")
    }

    private fun moveToTargetLocation(kakaoMap: KakaoMap, target: Location) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(target.y, target.x))
        kakaoMap.moveCamera(cameraUpdate)
        val labelManager = kakaoMap.labelManager
        labelManager?.let { setPin(labelManager, target) }
    }

    private fun showErrorPage(exception: Exception?) {
        binding.errorCode.text = exception?.message
        binding.errorLayout.isVisible = true
        binding.kakaoMapView.isVisible = false
        binding.searchBox.isVisible = false
    }

    private fun setPin(labelManager: LabelManager, target: Location) {
        labelManager.removeAllLabelLayer()
        val style = labelManager
            .addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.location_label).setTextStyles(30, Color.BLACK)
                )
            )
        labelManager.layer
            ?.addLabel(
                LabelOptions.from(LatLng.from(target.y, target.x))
                    .setStyles(style)
                    .setTexts(target.name)
            )
    }

    fun updateView(lastLocation: Location) {
        if (::kakaoMap.isInitialized) {
            binding.lastLoc = lastLocation
            moveToTargetLocation(kakaoMap, lastLocation)
            binding.infoSheet.isVisible = true
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showNotificationPermissionDialog()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.permission_request_notification_title))
            setMessage(
                String.format(
                    getString(R.string.permission_request_notification_message),
                    getString(R.string.app_name)
                )
            )
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.no)) { _, _ -> }
            show()
        }
    }
}