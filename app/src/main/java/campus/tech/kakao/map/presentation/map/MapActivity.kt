package campus.tech.kakao.map.presentation.map

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.search.SearchActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapBottomSheet: MapBottomSheet
    private lateinit var kakaoMap: KakaoMap
    private val mapViewModel: MapViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i("permission", "권한 획득")
        } else {
            Log.i("permission", "권한 거절")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setUI()
        collectViewModel()
        initMapView()
        setResultLauncher()
        askNotificationPermission()

    }

    private fun setUI(){
        setStatusBarTransparent()
        setSwipeListener()
        setSearchView()
    }

    private fun setStatusBarTransparent() {
        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.lifecycleOwner = this
        binding.viewModel = mapViewModel
    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            mapViewModel.lastVisitedPlace.collect { place ->
                place?.let {
                    updateMapWithPlaceData(it)
                    showBottomSheet(it)
                }
            }
        }
    }
    private fun setSwipeListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!isNetworkAvailable()) {
                showErrorPage(Exception("네트워크 연결 오류"))
            }else{
                showMapPage()
                showBottomSheet(mapViewModel.lastVisitedPlace.value)
                binding.swipeRefreshLayout.isEnabled = false
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun initMapView() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception) {
                showErrorPage(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                if (!isNetworkAvailable()) {
                    showErrorPage(Exception("네트워크 연결 오류"))
                }else{
                    binding.swipeRefreshLayout.isEnabled = false
                    initMapPage()
                }
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        return PlaceApplication.isNetworkActive(this)
    }

    private fun initMapPage(){
        showMapPage()
        mapViewModel.loadLastVisitedPlace()
    }

    private fun showMapPage(){
        binding.tvErrorMessage.visibility = View.GONE
        binding.btnSearch.visibility = View.VISIBLE
        binding.mapView.visibility = View.VISIBLE
    }

    private fun showErrorPage(error: Exception) {
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.mapView.visibility = View.GONE
        binding.btnSearch.visibility = View.GONE
        binding.tvErrorMessage.text = "지도 인증에 실패했습니다.\n다시 시도해주세요.\n" + error.message
    }

    private fun setSearchView() {
        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val placeData = result.data?.getSerializableExtra("placeData") as? Place
                placeData?.let {
                    mapViewModel.saveLastVisitedPlace(it)
                }
            }
        }
    }

    private fun updateMapWithPlaceData(place: Place) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(place.yPos.toDouble(), place.xPos.toDouble()), 15
        )
        kakaoMap.moveCamera(cameraUpdate)

        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(LabelStyle.from(R.drawable.icon_location4))
        )
        val options = LabelOptions.from(
            LatLng.from(place.yPos.toDouble(), place.xPos.toDouble())
        ).setStyles(styles)

        val layer = kakaoMap.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun showBottomSheet(place: Place?) {
        val bottomSheet = MapBottomSheet()
        place?.let {
            val args = Bundle()
            args.putSerializable("place", it)
            bottomSheet.arguments = args }
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 설명하는 UI를 표시
                showNotificationPermissionDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this).apply {
            setMessage(
                String.format(
                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림 에서 %s의 알림 권한을 허용해주세요.)",
                    getString(R.string.app_name)
                )
            )
            setPositiveButton("허용") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton("허용 안함") { _, _ -> }
            show()
        }
    }
}
