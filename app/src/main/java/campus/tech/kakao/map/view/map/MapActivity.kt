package campus.tech.kakao.map.view.map

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.databinding.ErrorMapBinding
import campus.tech.kakao.map.databinding.MapBottomSheetBinding
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.view.search.MainActivity
import campus.tech.kakao.map.viewmodel.LocationViewModel
import campus.tech.kakao.map.viewmodel.RemoteConfigViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import campus.tech.kakao.map.model.RemoteConfig

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var activityMapBinding: ActivityMapBinding
    private lateinit var errorMapBinding: ErrorMapBinding
    private lateinit var mapBottomSheetBinding: MapBottomSheetBinding
    private lateinit var splashScreen: SplashScreen

    companion object {
        private const val DEFAULT_LONGITUDE = 127.115587
        private const val DEFAULT_LATITUDE = 37.406960
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMapBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(activityMapBinding.root)

        errorMapBinding = ErrorMapBinding.inflate(layoutInflater)
        mapBottomSheetBinding = activityMapBinding.mapBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(mapBottomSheetBinding.bottomSheetLayout)
        setupMapView()
        setupEditText()

        createChannel(
            BuildConfig.CHANNEL_ID,
            BuildConfig.CHANNEL_NAME
        )
        askNotificationPermission()
    }

    private fun updateConfigs(remoteConfig: RemoteConfig) {
    }

    override fun onResume() {
        super.onResume()
        activityMapBinding.mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        activityMapBinding.mapView.pause() // MapView 의 pause 호출
    }

    private fun setupEditText() {
        activityMapBinding.searchEditTextInMap.setOnClickListener {
            val intent: Intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMapView() {
        activityMapBinding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("jieun", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {  // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("jieun", "onMapError$error")
                showErrorMessage(error)
            }
        }, object : KakaoMapReadyCallback() {
            val location = getLocation()
            override fun onMapReady(kakaoMap: KakaoMap) { // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("jieun", "onMapReady location: " + location.toString())
                if (location != null) {
                    showLabel(location, kakaoMap)
                    showBottomSheet(location)
                    locationViewModel.addLastLocation(location)
                } else {
                    hideBottomSheet()
                }
            }

            override fun getPosition(): LatLng {
                if (location != null) {
                    return LatLng.from(location.latitude, location.longitude)
                } else {
                    return LatLng.from(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
                }

            }

        })
    }

    private fun showErrorMessage(error: Exception) {
        runOnUiThread {
            setContentView(errorMapBinding.root)
            errorMapBinding.errorMessageTextView.text =
                "지도 인증을 실패했습니다.\n다시 시도해주세요.\n\n" + error.message
        }
    }

    private fun showLabel(
        location: Location,
        kakaoMap: KakaoMap
    ) {
        val labelStyles: LabelStyles = LabelStyles.from(
            LabelStyle.from(R.drawable.location_red_icon_resized).setZoomLevel(8),
            LabelStyle.from(R.drawable.location_red_icon_resized)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
        )
        val position = LatLng.from(location.latitude, location.longitude)
        kakaoMap.labelManager?.layer?.addLabel(
            LabelOptions.from(position)
                .setStyles(labelStyles)
                .setTexts(location.title)
        )
    }

    private fun hideBottomSheet() {
        mapBottomSheetBinding.bottomSheetLayout.visibility = View.GONE
    }

    private fun showBottomSheet(location: Location) {
        mapBottomSheetBinding.bottomSheetLayout.visibility = View.VISIBLE
        mapBottomSheetBinding.bottomSheetTitle.text = location.title
        mapBottomSheetBinding.bottomSheetAddress.text = location.address
        mapBottomSheetBinding.bottomSheetCategory.text = location.category
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getLocation(): Location? {
        var location = getLocationByIntent()
        if (location == null) {
            location = locationViewModel.getLastLocation()
        }
        return location

    }

    private fun getLocationByIntent(): Location? {
        if (intent.hasExtra("location")) {
            val location =
                intent.getParcelableExtra("location", Location::class.java) // API 레벨 오류, 실행에는 문제없다.
            Log.d("jieun", "getLocationByIntent location " + location.toString())
            return location
        }
        return null
    }

    // 6주차
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 설명하는 UI를 표시
                showNotificationPermissionDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this@MapActivity).apply {
            setTitle("알림 권한 요청")
            setMessage(
                String.format(
                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림 에서 %s의 알림 권한을 허용해주세요.)",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton("네") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton("아니요") { _, _ -> }
            show()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "알림이 허용되었습니다.", Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(this, "알림이 거부되었습니다.", Toast.LENGTH_SHORT)
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "커피 이벤트 홍보"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}