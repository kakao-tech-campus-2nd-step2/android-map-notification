package campus.tech.kakao.map.data.repository.place

import android.content.SharedPreferences
import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.data.remote.api.KakaoApiService
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val kakaoApiService: KakaoApiService
): PlaceRepositoryInterface {
    override suspend fun searchPlaces(query: String): List<Place>{
        val apiKey = KAKAOAK + BuildConfig.KAKAO_REST_API_KEY

        return try {
            val response = kakaoApiService.getPlace(apiKey, query)
            val documentList = response.documents ?: emptyList()
            documentList.map {
                Place(
                    name = it.placeName,
                    location = it.addressName,
                    category = it.categoryGroupName,
                    x = it.x,
                    y = it.y
                )
            }
        } catch (e: Exception) {
            Log.e("KakaoAPI", "Failure: ${e.message}")
            emptyList()
        }
    }

    override fun saveLastLocation(item: Place) {
        with(sharedPreferences.edit()) {
            putString("PLACE_X", item.x)
            putString("PLACE_Y", item.y)
            apply()
        }
    }

    companion object {
        private const val KAKAOAK = "KakaoAK "
    }
}