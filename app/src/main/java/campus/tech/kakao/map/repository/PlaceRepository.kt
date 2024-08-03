package campus.tech.kakao.map.repository

import androidx.lifecycle.LiveData
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.remote.RetrofitData
import javax.inject.Inject

class PlaceRepository @Inject constructor(
	private val retrofitData: RetrofitData
):PlaceRepositoryInterface {

	override fun searchPlace(query: String): LiveData<List<Document>> {
		retrofitData.searchPlace(query)
		return retrofitData.getDocuments()
	}
}