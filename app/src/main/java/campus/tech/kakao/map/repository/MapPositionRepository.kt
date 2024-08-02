package campus.tech.kakao.map.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.mapPosition.MapPositionPreferences
import campus.tech.kakao.map.viewModel.MainViewModel
import javax.inject.Inject

class MapPositionRepository @Inject constructor(
	private val mapPosition: MapPositionPreferences
):MapPositionRepositoryInterface{

	private val _mapInfoList = MutableLiveData<List<String>>()

	fun getMapInfoList():LiveData<List<String>>{
		return _mapInfoList
	}

	override fun setMapInfo(document: Document) {
		mapPosition.setMapInfo(document)
		getMapInfo()
	}

	override fun getMapInfo() {
		_mapInfoList.value = listOf()
		val latitude = mapPosition.getPreferences(
			MainViewModel.LATITUDE,
			MainViewModel.INIT_LATITUDE
		)
		val longitude = mapPosition.getPreferences(
			MainViewModel.LONGITUDE,
			MainViewModel.INIT_LONGITUDE
		)
		val placeName = mapPosition.getPreferences(MainViewModel.PLACE_NAME,"")
		val addressName = mapPosition.getPreferences(MainViewModel.ADDRESS_NAME,"")
		_mapInfoList.value = listOf(latitude, longitude, placeName, addressName)
	}
}