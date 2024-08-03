package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.remote.RetrofitData
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PlaceRepository @Inject constructor(
	private val retrofitData: RetrofitData
):PlaceRepositoryInterface {

	override fun searchPlace(query: String): StateFlow<List<Document>> {
		retrofitData.searchPlace(query)
		return retrofitData.documents
	}
}