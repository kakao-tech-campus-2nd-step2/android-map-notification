package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.mapPosition.MapPositionPreferences
import campus.tech.kakao.map.viewModel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MapPositionRepository @Inject constructor(
	private val mapPosition: MapPositionPreferences
):MapPositionRepositoryInterface{

	private val _mapInfoList = MutableStateFlow<List<String>>(emptyList())
	val mapInfoList: StateFlow<List<String>> = _mapInfoList.asStateFlow()

	override fun setMapInfo(document: Document) {
		mapPosition.setMapInfo(document)
		getMapInfo()
	}

	override fun getMapInfo() {
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