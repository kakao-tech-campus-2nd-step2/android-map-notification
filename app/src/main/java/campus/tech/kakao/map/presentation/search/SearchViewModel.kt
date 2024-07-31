package campus.tech.kakao.map.presentation.search

import androidx.lifecycle.*
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor( private val repository: PlaceRepository) : ViewModel() {

    val searchText = MutableLiveData<String>()

    private val _logList = MutableStateFlow<List<Place>>(emptyList())
    val logList: StateFlow<List<Place>> get() = _logList.asStateFlow()

    private val _searchedPlaces = searchText.asFlow()
        .debounce(500L)
        .flatMapLatest { keyword ->
            if (keyword.isNotBlank()) {
                   flowOf(getPlaces(keyword))
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val searchedPlaces: StateFlow<List<Place>> get() = _searchedPlaces

    init {
        viewModelScope.launch {
            _logList.value = getLogs()
        }
    }

    fun clearSearch() {
        searchText.value = ""
    }
    
    private suspend fun getPlaces(keyword: String): List<Place> {

        val resultPlaces = mutableListOf<Place>()

        for(page in 1..3){
            val places = getPlacesByPage(keyword,page)
            resultPlaces.addAll(places)
        }

        repository.updatePlaces(resultPlaces)
        return resultPlaces
    }
    private suspend fun getPlacesByPage(keyword: String, page: Int): List<Place> {
        return repository.getPlaces(keyword, page)
    }

    suspend fun getPlaceById(id: String): Place? {
        return repository.getPlaceById(id)
    }

    private suspend fun getLogs(): List<Place> {
        return repository.getLogs()
    }

    fun updateLogs(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedList = _logList.value.toMutableList()
            val existingLog = updatedList.find { it.id == place.id }
            if (existingLog != null) {
                updatedList.remove(existingLog)
                updatedList.add(0, existingLog)
            } else {
                updatedList.add(0, place)
            }
            _logList.value = updatedList
            repository.updateLogs(updatedList)
        }
    }

    fun removeLog(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeLog(id)
            _logList.value = getLogs()
        }
    }
}
