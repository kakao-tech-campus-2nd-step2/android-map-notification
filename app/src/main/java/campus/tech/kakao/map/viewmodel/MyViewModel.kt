package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.data.Location
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch
import campus.tech.kakao.map.model.data.toLocation
import campus.tech.kakao.map.model.network.KakaoSearchResponse
import campus.tech.kakao.map.model.repository.MyRepository
import campus.tech.kakao.map.view.PlaceAdapter
import campus.tech.kakao.map.view.SavedSearchAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repository: MyRepository) : ViewModel() {

    private val _searchText = MutableLiveData<String>()  //검색어를 입력하는 editText
    val searchText get() = _searchText

    private val _isIntent: MutableLiveData<Boolean> = MutableLiveData(false) //위치한 액티비티를 나타내는 변수
    val isIntent get() = _isIntent

    private val _placeAdapterUpdateData =
        MutableLiveData<List<Place>>() //업데이트 해야하는 PlaceAdapter List<Place>
    val placeAdapterUpdateData get() = _placeAdapterUpdateData

    private val _savedSearchAdapterUpdateData =
        MutableLiveData<List<SavedSearch>>() //업데이트 해야하는 SavedSearchAdapter List<SavedSearch)
    val savedSearchAdapterUpdateData get() = _savedSearchAdapterUpdateData

    private val _itemClick = MutableLiveData<Place>() //Place의 item
    val itemClick get() = _itemClick

    private val _nameClick = MutableLiveData<SavedSearch>() //savedSearch의 이름 부분
    val nameClick get() = _nameClick

    private val _location = MutableLiveData<Location>()
    val location get() = _location



    fun insertSavedSearch(place: Place){
        viewModelScope.launch {
            repository.insertSavedSearch(SavedSearch(id = place.id, name = place.name))
        }
    }

    fun setSharedPreferences(place: Place){
        repository.setSharedPreferences(place.toLocation())
    }

    fun itemClick(place: Place){
        _itemClick.value = place
    }

    fun nameClick(savedSearch: SavedSearch){
        _nameClick.value = savedSearch
    }


    //SavedSearchAdapter 초기화
//    val vmSavedSearchAdapter: SavedSearchAdapter = SavedSearchAdapter(listOf(),
//        onCloseClick = { SavedSearch -> //SavedSearch의 x를 누르면
//            viewModelScope.launch {
//                repository.deleteSavedSearch(SavedSearch.id)  // SavedSearch item 삭제
//                updateSavedSearch() // SavedSearch UI 업데이트
//            }
//        },
//        onNameClick = { SavedSearch ->   //SavedSearch의 이름을 누르면
//            _nameClick.value = SavedSearch   //화면에 보이는 text 설정
//            _searchText.value = SavedSearch.name //검색 쿼리
//        }
//    )

    fun deleteSavedSearch(id : Int){
        viewModelScope.launch {
            repository.deleteSavedSearch(id)
        }
    }

    fun setSearchText(savedSearch: SavedSearch){
        _searchText.value = savedSearch.name
    }





    // true일 때 SearchPlaceActivity에 위치하고있음
    fun intentSearchPlace() {
        _isIntent.value = true
    }

    //editText를 지우는 closeIcon 클릭이벤트
    fun clickCloseIcon() {
        //햅틱 진동 기능 추가하고 싶다..
        _searchText.value = " " //editText빈칸으로 만들기
    }

    //(비동기) 카카오 키워드 검색, 검색 결과는 placeAdapterUpdateData에 List<Place>로 저장
    fun searchPlace(query: String) {
        viewModelScope.launch {
            try {
                val response: Response<KakaoSearchResponse> = repository.searchKeyword(query)
                if (response.isSuccessful) {
                    _placeAdapterUpdateData.value = response.body()?.documents?.map { document ->
                        Place(
                            id = document.id.toInt(),
                            name = document.place_name,
                            address = document.address_name,
                            kind = document.category_name,
                            longitude = document.x, //경도
                            latitude = document.y   //위도
                        )
                    } ?: emptyList()
                } else {
                    Log.e("MyViewModel", "Search failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Exception in searchPlace", e)
            }
        }
    }


    //Repository에서 List(SavedSearch) 가져와서  savedSearchAdapterUpdateData에 저장
    fun updateSavedSearch() {
        viewModelScope.launch {
            _savedSearchAdapterUpdateData.value = repository.getSavedSearches()
        }
        Log.d("seyoung", _savedSearchAdapterUpdateData.value.toString())
    }

    fun getSharedPreferences() {
        viewModelScope.launch {
            _location.value = repository.getSharedPreferences()
        }
    }


}