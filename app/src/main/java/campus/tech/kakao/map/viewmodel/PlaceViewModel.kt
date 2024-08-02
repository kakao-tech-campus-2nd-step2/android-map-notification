// PlaceViewModel.kt
package campus.tech.kakao.map.viewmodel

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Document
import campus.tech.kakao.map.model.PlaceData
import campus.tech.kakao.map.model.RetrofitService
import campus.tech.kakao.map.view.MainActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val retrofitService: RetrofitService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {


    // search
    companion object { private const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}" }

    private val _places = MutableLiveData<List<Document>>()
    private val _savedQueries = MutableLiveData<MutableList<String>>()
    val places: LiveData<List<Document>> get() = _places
    val savedQueries: LiveData<MutableList<String>> get() = _savedQueries

    private val _placeData = MutableLiveData<PlaceData>()
    val placeData: LiveData<PlaceData> get() = _placeData

    init {
        _savedQueries.value = loadSavedQueries()
        loadPlacePreferences()
    }

    fun loadPlaces(query: String, categoryGroupName: String) {
        viewModelScope.launch {
            try {
                val response = retrofitService.getPlaces(API_KEY, categoryGroupName, query)
                _places.value = response.documents
            } catch (e: Exception) {
                Log.e("PlaceViewModel", "Failed to fetch places: ${e.message}")
                _places.value = emptyList()
            }
        }
    }

    fun addSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList().apply { add(query) }
        _savedQueries.value = updatedList
        saveQueries(updatedList)
    }

    fun removeSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList().apply { remove(query) }
        _savedQueries.value = updatedList
        saveQueries(updatedList)
    }

    private fun loadSavedQueries(): MutableList<String> {
        val savedQueriesString = sharedPreferences.getString("queries", null)
        return if (!savedQueriesString.isNullOrEmpty()) {
            savedQueriesString.split(",").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private fun saveQueries(queries: MutableList<String>) {
        sharedPreferences.edit().apply {
            putString("queries", queries.joinToString(","))
            apply()
        }
    }

    // main
    private fun loadPlacePreferences() {
        viewModelScope.launch {
            val placeName = sharedPreferences.getString(MainActivity.EXTRA_PLACE_NAME, "Unknown Place") ?: "Unknown Place"
            val addressName = sharedPreferences.getString(MainActivity.EXTRA_PLACE_ADDRESSNAME, "Unknown Address") ?: "Unknown Address"
            val longitude = sharedPreferences.getString(MainActivity.EXTRA_PLACE_LONGITUDE, "127.108621")?.toDouble() ?: 0.0
            val latitude = sharedPreferences.getString(MainActivity.EXTRA_PLACE_LATITUDE, "37.402005")?.toDouble() ?: 0.0

            _placeData.value = PlaceData(
                longitude,
                latitude,
                placeName,
                addressName
            )
        }
    }


    // splash
    private val _toMap = MutableLiveData<Boolean>()
    val toMap: LiveData<Boolean> get() = _toMap

    val serviceMessage = MutableLiveData<String>()
    val image = MutableLiveData<String>()

    init {
        _toMap.value = false
        image.value = "@drawable/ic_launcher_foreground"
    }

    // remote config
    fun fetchRemoteConfig(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val serviceMessage = remoteConfig.getString("serviceMessage")
                    val serviceState = remoteConfig.getString("serviceState")
                    updateConfig(serviceState, serviceMessage)
                } else {
                    updateConfig(
                        remoteConfig.getString("serviceMessage"),
                        remoteConfig.getString("serviceState")
                    )
                }
            }
    }
    
    // on_service가 아니면 다른 화면, 5초후
    private fun updateConfig(serviceState: String, serviceMessage: String) {
        if (serviceState == "ON_SERVICE") {
            Handler(Looper.getMainLooper()).postDelayed({
                _toMap.value = true
            }, 5000)
        } else {
            this.serviceMessage.value = serviceMessage
            Handler(Looper.getMainLooper()).postDelayed({
                _toMap.value = false
            }, 5000)
        }
    }


    
}
