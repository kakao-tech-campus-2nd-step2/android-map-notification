package campus.tech.kakao.map.repository

import campus.tech.kakao.map.base.MyApplication
import campus.tech.kakao.map.data.db.AppDatabase
import campus.tech.kakao.map.data.db.PlaceDao
import campus.tech.kakao.map.data.db.entity.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogRepository @Inject constructor (private val application: MyApplication, private val placeDao: PlaceDao): LogRepositoryInterface {
    override suspend fun getAllLogs(): List<Place> {
        return placeDao.getAllLogs()
    }

    override suspend fun haveAnyLog(): Boolean {
        return placeDao.getPlaceCount() > 0
    }

    override suspend fun insertLog(place: Place) {
        placeDao.insertLog(place)
    }

    override suspend fun deleteLog(place: Place) {
        placeDao.deleteLog(place)
    }
}