package campus.tech.kakao.map.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.model.Place
import campus.tech.kakao.map.data.model.RecentSearchWord
import campus.tech.kakao.map.data.repository.MapRepository
import campus.tech.kakao.map.data.vo.Config
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: MapRepository) : ViewModel() {

    private val _config: MutableLiveData<Config> = MutableLiveData<Config>()
    val config: LiveData<Config> get()= _config

    private val _places: MutableLiveData<List<Place>> = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> get()= _places

    private val _searchHistoryData: MutableLiveData<ArrayList<RecentSearchWord>> =
        MutableLiveData<ArrayList<RecentSearchWord>>()
    val searchHistoryData: LiveData<ArrayList<RecentSearchWord>> get() = _searchHistoryData

    init {
        _searchHistoryData.value = repository.searchHistoryList
        setLocalDB()
        getConfig()
    }
    fun getConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            val fireDbConfig = repository.getConfig()
            _config.postValue(fireDbConfig)
        }
    }

    fun searchPlaces(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("search", "search: ${Thread.currentThread().name}")
            if (search.isEmpty()) {
                _places.value = mutableListOf()
                return@launch
            }
                _places.postValue(repository.searchPlaces(search).getOrDefault(emptyList()))
                Log.d("search", "search2: ${Thread.currentThread().name}")
            }
    }

    fun searchRoomPlaces(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (search.isEmpty()) {
                _places.postValue(mutableListOf())
                return@launch
            }
            repository.searchRoomPlaces(search) { placeList ->
                _places.postValue(placeList)
                Log.d("search", "search: ${Thread.currentThread().name}")
            }
        }
    }

    private fun setLocalDB() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRoomInitialData()
            repository.setPrefs()
        }
    }


    /**
     * Pref 관련
     */
    suspend fun getLastPos(): LatLng? {
        return repository.getLastPos()
    }

    fun savePos(longitude: String, latitude: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("prefs", "savePos: ${Thread.currentThread().name}")
            repository.savePos(LatLng.from(latitude.toDouble(), longitude.toDouble()))
        }
    }

    fun insertSearch(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val foundIdx = repository.searchHistoryContains(search)
            if (foundIdx != -1) {
                repository.moveSearchToLast(foundIdx, search)
            } else {
                repository.addSearchHistory(search)
            }
            _searchHistoryData.postValue(repository.searchHistoryList)
            Log.d("prefs", "insetSearch: ${Thread.currentThread().name}")
        }
    }

    fun delSearch(idx: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delSearchHistory(idx)
            _searchHistoryData.postValue(repository.searchHistoryList)
            Log.d("prefs", "delSearch: ${Thread.currentThread().name}")
        }
    }
}