package campus.tech.kakao.map.repository

import campus.tech.kakao.map.ApiKey
import campus.tech.kakao.map.model.KakaoLocalService
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.model.SavePlace
import campus.tech.kakao.map.model.SavePlaceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val retrofit: KakaoLocalService,
    private val savePlaceDao: SavePlaceDao,
    @ApiKey private val apiKey: String
) : SearchRepository {

    override suspend fun savePlaces(placeName: String): List<SavePlace> {
        withContext(Dispatchers.IO) {
            val existingPlace = savePlaceDao.getByName(placeName)
            if (existingPlace != null) {
                savePlaceDao.delete(existingPlace)
            }
            savePlaceDao.insert(SavePlace(savePlaceName = placeName))
        }
        return showSavePlace()
    }

    override suspend fun showSavePlace(): List<SavePlace> {
        return withContext(Dispatchers.IO) {
            savePlaceDao.getAll()
        }
    }

    override suspend fun deleteSavedPlace(savedPlaceName: String): List<SavePlace> {
        withContext(Dispatchers.IO) {
            val existingPlace = savePlaceDao.getByName(savedPlaceName)
            if (existingPlace != null) {
                savePlaceDao.delete(existingPlace)
            }
        }
        return showSavePlace()
    }

    override suspend fun getPlaceList(categoryGroupName: String): List<PlaceInfo>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.getPlaceList(apiKey, categoryGroupName).execute()
                if (response.isSuccessful) {
                    response.body()?.documents
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
