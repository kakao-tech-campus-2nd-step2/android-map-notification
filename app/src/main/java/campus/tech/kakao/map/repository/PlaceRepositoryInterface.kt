package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document
import kotlinx.coroutines.flow.StateFlow

interface PlaceRepositoryInterface {
	fun searchPlace(query: String): StateFlow<List<Document>>
}