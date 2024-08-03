package campus.tech.kakao.map.data.remote

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.document.PlaceResponse
import campus.tech.kakao.map.data.remote.UrlContract.AUTHORIZATION
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PlaceRemoteDataSource @Inject constructor(private val retrofitService: RetrofitService) {

	interface LoadPlaceCallback {
		fun onPlaceLoaded(documents: List<Document>)
		fun onDataNotAvailable()
	}

	fun searchPlace(query: String, callback: LoadPlaceCallback) {
		retrofitService.requestPlaces(AUTHORIZATION, query).enqueue(object : Callback<PlaceResponse> {
			override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
				if (response.isSuccessful) {
					val documentList = response.body()?.documents?.map { document ->
						Document(
							document.placeName,
							document.categoryGroupName,
							document.addressName,
							document.longitude,
							document.latitude
						)
					} ?: emptyList()
					callback.onPlaceLoaded(documentList)
				} else {
					callback.onDataNotAvailable()
				}
			}

			override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
				callback.onDataNotAvailable()
			}
		})
	}
}