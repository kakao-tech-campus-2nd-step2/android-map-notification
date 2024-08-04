package campus.tech.kakao.map

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchHistory: SearchHistoryRepository
) : ViewModel() {

    private val _placeInfo = MutableLiveData<MainPlaceInfo>()
    val placeInfo: LiveData<MainPlaceInfo>
        get() = _placeInfo

    private val _isBottomSheetVisible = MutableLiveData<Boolean>(false)
    val isBottomSheetVisible: LiveData<Boolean>
        get() = _isBottomSheetVisible

    val location: LiveData<LatLng?> = searchHistory.getAllSearchHistories().asLiveData().switchMap { historyList ->
        MutableLiveData<LatLng?>().apply {
            value = if (historyList.isEmpty()) {
                null
            } else {
                val historyLongitude = historyList[0].x?.toDoubleOrNull()
                val historyLatitude = historyList[0].y?.toDoubleOrNull()
                if (historyLongitude != null && historyLatitude != null) {
                    LatLng.from(historyLatitude, historyLongitude)
                } else {
                    null
                }
            }
        }
    }

    fun updatePlaceInfo(name: String?, address: String?) {
        if (!name.isNullOrEmpty() && !address.isNullOrEmpty()) {
            _placeInfo.value = MainPlaceInfo(name, address)
            _isBottomSheetVisible.value = true
        } else {
            _isBottomSheetVisible.value = false
        }
    }
}
