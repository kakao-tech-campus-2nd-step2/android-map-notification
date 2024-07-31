package campus.tech.kakao.map.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.repository.location.KakaoLocationImpl
import campus.tech.kakao.map.repository.location.LocationRepository
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel(),
    OnSearchItemClickListener {
    private val _items = MutableLiveData<List<LocalUiModel>>()
    val items: LiveData<List<LocalUiModel>>
        get() = _items

    private val _localInformation = MutableLiveData<LocalUiModel>()
    val localInformation: LiveData<LocalUiModel>
        get() = _localInformation

    init {
        if (locationRepository is KakaoLocationImpl) {
            locationRepository.items.observeForever {
                _items.value = it
            }
        }
    }

    fun searchLocationData(keyword: String) {
        if (keyword.isNotEmpty()) {
            locationRepository.search(keyword)
        } else {
            _items.value = listOf()
        }
    }

    override fun onSearchItemClick(localUiModel: LocalUiModel) {
        _localInformation.value = localUiModel
    }
}
