package campus.tech.kakao.map.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ErrorLayoutBinding
import campus.tech.kakao.map.databinding.MapLayoutBinding
import campus.tech.kakao.map.domain.Place
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var mapBinding: MapLayoutBinding
    private lateinit var labelManager: LabelManager
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel: MapViewModel by viewModels()
    private var kakaoMap: KakaoMap ?= null

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            this@MapActivity.kakaoMap = kakaoMap
            labelManager = kakaoMap.labelManager!!
            displayMapLocation()
        }
    }

    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {

        override fun onMapDestroy() {
            Toast.makeText(applicationContext, "onMapDestroy", Toast.LENGTH_SHORT).show()
        }

        override fun onMapError(error: Exception) {
            viewModel.onMapError(error)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        // Log "notification" 을 통해 토큰 확인
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("notification", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("notification", msg)
        })

        mapBinding = DataBindingUtil.setContentView(this, R.layout.map_layout)
        mapBinding.mapView.start(lifeCycleCallback, readyCallback)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                labelManager.removeAllLabelLayer()
                val place = getIntentData(result)
                viewModel.updateLocationFromIntent(place)
                viewModel.saveLocation()
                displayUpdatedLocation()
            }
        }

        mapBinding.etSearch.setOnClickListener {
            val intent = Intent(this, PlaceActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        viewModel.errorMessage.observe(this, Observer { message ->
            if (message != null) {
                val errorBinding: ErrorLayoutBinding = DataBindingUtil.setContentView(this, R.layout.error_layout)
                errorBinding.viewModel = viewModel
                errorBinding.lifecycleOwner = this
            }
        })
    }

    private fun displayUpdatedLocation() {
        displayMapLocation()
        displayBottomSheet()
        displayMarker()
    }

    private fun getIntentData(result: ActivityResult): Place {
        val name = result.data!!.getStringExtra("name")
        val address = result.data!!.getStringExtra("address")
        val category = result.data?.getStringExtra("category")
        val latitude = result.data?.getStringExtra("latitude")
        val longitude = result.data?.getStringExtra("longitude")
        val place = Place(name!!, address!!, category, latitude, longitude)
        return place
    }

    override fun onResume() {
        super.onResume()
        mapBinding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapBinding.mapView.pause()
    }

    private fun displayMapLocation() {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(viewModel.longitude.value!!.toDouble(), viewModel.latitude.value!!.toDouble())
        )
        kakaoMap?.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }

    private fun displayMarker() {
        val pos = LatLng.from(viewModel.longitude.value!!.toDouble(), viewModel.latitude.value!!.toDouble())
        val yellowMarker = labelManager.addLabelStyles(LabelStyles.from("yellowMarker", LabelStyle.from(R.drawable.yellow_marker)))
        labelManager.layer!!.addLabel(
            LabelOptions.from(pos).setStyles(yellowMarker)
        )
    }

    private fun displayBottomSheet() {
        val dataBundle = Bundle().apply {
            putString("name", viewModel.name.value)
            putString("address", viewModel.address.value)
        }
        val modal = ModalBottomSheet()
        modal.arguments = dataBundle
        modal.show(supportFragmentManager, "modalBottomSheet")
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(), ) {
            isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "FCM can't post notifications without POST_NOTIFICATIONS permission", Toast.LENGTH_LONG).show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            }
            else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 설명하는 UI를 표시
                showNotificationPermissionDialog()
            }
            else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this@MapActivity).apply {
            setTitle("알림 권한 설정")
            setMessage(
                String.format("다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림에서 %s의 알림 권한을 허용해주세요.)", getString(R.string.app_name))
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

}
