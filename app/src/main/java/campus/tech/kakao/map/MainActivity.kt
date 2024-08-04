package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.google.firebase.messaging.FirebaseMessaging

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewBinding: ActivityMainBinding
    private lateinit var mapView: MapView
    @Inject lateinit var preferenceManager: PreferenceManager
    @Inject lateinit var mapNotificationManager: MapNotificationManager

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewBinding.activity = this
        mainViewBinding.viewModel = mainViewModel
        mainViewBinding.lifecycleOwner = this

        // requestPermissionLauncher 초기화
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 허용되었을 때 수행할 작업
                mapNotificationManager.askNotificationPermission(requestPermissionLauncher)
            } else {
                Toast.makeText(this@MainActivity, "알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // Firebase 토큰 요청
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("MainActivity", "FCM Registration token: $token")
        }

        var latitude = intent?.getStringExtra("latitude")?.toDoubleOrNull()
        var longitude = intent?.getStringExtra("longitude")?.toDoubleOrNull()
        val name = intent?.getStringExtra("name")
        val address = intent?.getStringExtra("address")

        mapView = mainViewBinding.mapView

        mainViewModel.location.observe(this@MainActivity, Observer { position ->
            if (position != null) {
                latitude = position.latitude
                longitude = position.longitude
            }
        })

        setUpMapView(mapView, longitude, latitude)
        mainViewModel.updatePlaceInfo(name, address)

        // 알림 권한 요청
        mapNotificationManager.askNotificationPermission(requestPermissionLauncher)

        // ForegroundService 시작
        startForegroundService()
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val latitude = intent.getStringExtra("latitude")?.toDoubleOrNull()
        val longitude = intent.getStringExtra("longitude")?.toDoubleOrNull()
        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")

        setUpMapView(mapView, longitude, latitude)
        mainViewModel.updatePlaceInfo(name, address)
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()

    }

    private fun setUpMapView(mapView: MapView, longitude: Double?, latitude: Double?) {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 파괴 시 처리
            }

            override fun onMapError(error: Exception) {
                val intent = Intent(this@MainActivity, ErrorActivity::class.java)
                intent.putExtra("Error", error.localizedMessage)
                startActivity(intent)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                val labelManager = kakaoMap.labelManager
                val styles: LabelStyles? = labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_pin)))
                val options = LabelOptions.from(position).setStyles(styles)
                val layer = kakaoMap.labelManager?.layer
                val label = layer?.addLabel(options)
            }

            override fun isVisible(): Boolean {
                return true
            }

            override fun getZoomLevel(): Int {
                return 18
            }

            override fun getPosition(): LatLng {
                return if (latitude != null && longitude != null) {
                    LatLng.from(latitude, longitude)
                } else super.getPosition()
            }
        })
    }

    fun onSearchClick() {
        val intent = Intent(this@MainActivity, SearchActivity::class.java)
        startActivity(intent)
    }
}
