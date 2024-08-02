package campus.tech.kakao.map.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var binding: ActivityMapBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.viewModel = mapViewModel
        binding.lifecycleOwner = this

        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.clickedPlaceView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        setListeners()
        observeViewModel()
        initializeMap()
        askNotificationPermission()
//        FirebaseMessaging.getInstance().token
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    return@OnCompleteListener
//                }
//
//                // Get new FCM registration token
//                val token = task.result
//                Log.d("testt", "FCM Registration token: $token")
//            })
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
        mapViewModel.saveLastPosition()
    }

    private fun setListeners() {
        binding.searchFloatingBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        mapViewModel.pos.observe(this) { pos ->
            pos?.let {
                moveClickedPlace(it.latitude, it.longitude)
            }
        }

        mapViewModel.clickedPlaceInfo.observe(this) { placeInfo ->
            placeInfo?.let {
                showClickedPlaceInfo(it.place_name, it.road_address_name)
                showLabel(it.y.toDouble(), it.x.toDouble())
            }
        }
    }

    private fun initializeMap() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.e("KakaoMap", "onMapError: ", error)
                showErrorView(error.message ?: "Unknown error")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                mapViewModel.setKakaoMap(map)
                mapViewModel.getLastPosition()
                handleIntent()
            }
        })
    }

    private fun handleIntent() {
        val clickedPlaceInfo = intent.getParcelableExtra<PlaceInfo>("placeInfo")
        clickedPlaceInfo?.let {
            mapViewModel.setClickedPlaceInfo(it)
        }
    }

    private fun showClickedPlaceInfo(clickedPlaceName: String?, clickedPlaceAddress: String?) {
        binding.clickedPlaceName.text = clickedPlaceName
        binding.clickedPlaceAddress.text = clickedPlaceAddress
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showLabel(latitude: Double, longitude: Double) {
        val kakaoMap = mapViewModel.kakaoMap.value ?: return
        val styles = kakaoMap.labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.pink_marker)))
        val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles)
        val layer = kakaoMap.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun moveClickedPlace(latitude: Double, longitude: Double) {
        val kakaoMap = mapViewModel.kakaoMap.value ?: return
        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude)))
    }

    private fun showErrorView(error: String) {
        binding.errorView.showError(error)
        binding.mapView.visibility = View.GONE
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
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
        AlertDialog.Builder(this@MapActivity).apply {
            setTitle(getString(R.string.ask_notification_permission_dialog_title))
            setMessage(
                String.format(
                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림 에서 %s의 알림 권한을 허용해주세요.)",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.deny_notification_permission)) { _, _ -> }
            show()
        }
    }



}
