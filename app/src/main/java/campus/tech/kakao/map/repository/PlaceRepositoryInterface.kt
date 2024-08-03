package campus.tech.kakao.map.repository

import androidx.lifecycle.LiveData
import campus.tech.kakao.map.data.document.Document

interface PlaceRepositoryInterface {
	fun searchPlace(query: String): LiveData<List<Document>>
}