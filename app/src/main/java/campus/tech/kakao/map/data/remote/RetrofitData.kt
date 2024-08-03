package campus.tech.kakao.map.data.remote

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.document.PlaceResponse
import campus.tech.kakao.map.data.remote.UrlContract.AUTHORIZATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetrofitData @Inject constructor(private val retrofitService: RetrofitService) {
	private val _documents = MutableStateFlow<List<Document>>(emptyList())
	val documents : StateFlow<List<Document>> = _documents.asStateFlow()

	fun searchPlace(query : String){
		retrofitService.requestPlaces(AUTHORIZATION,query).enqueue(object : Callback<PlaceResponse> {
			override fun onResponse(
				call: Call<PlaceResponse>,
				response: Response<PlaceResponse>
			) {
				if (response.isSuccessful) {
					val documentList = mutableListOf<Document>()
					val body = response.body()
					body?.documents?.forEach {document ->
						documentList.add(
							Document(
							document.placeName,
							document.categoryGroupName,
							document.addressName,
							document.longitude,
							document.latitude)
						)
					}
					_documents.value = documentList
				}
			}

			override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
			}
		})
	}
}