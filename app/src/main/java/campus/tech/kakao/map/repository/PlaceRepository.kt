package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.remote.PlaceRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PlaceRepository @Inject constructor(
	private val placeRemoteDataSource: PlaceRemoteDataSource
) {
	private val _documentList = MutableStateFlow<List<Document>>(emptyList())
	val documentList: StateFlow<List<Document>> = _documentList.asStateFlow()

	fun searchPlace(query: String) {
		placeRemoteDataSource.searchPlace(query, object : PlaceRemoteDataSource.LoadPlaceCallback {
			override fun onPlaceLoaded(documents: List<Document>) {
				_documentList.value = documents
			}

			override fun onDataNotAvailable() {
				_documentList.value = emptyList()
			}
		})
	}
}