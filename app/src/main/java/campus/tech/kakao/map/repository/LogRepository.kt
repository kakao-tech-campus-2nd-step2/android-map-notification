package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.db.PlaceDao
import campus.tech.kakao.map.data.db.entity.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogRepository @Inject constructor (private val placeDao: PlaceDao): LogRepositoryInterface {
    override suspend fun getAllLogs(): List<Place> = withContext(Dispatchers.IO) {
        placeDao.getAllLogs()
    }

    override suspend fun haveAnyLog(): Boolean = withContext(Dispatchers.IO) {
        placeDao.getPlaceCount() > 0
    }

    override suspend fun insertLog(place: Place) = withContext(Dispatchers.IO) {
        placeDao.insertLog(place)
    }

    override suspend fun deleteLog(place: Place) = withContext(Dispatchers.IO) {
        placeDao.deleteLog(place)
    }
}