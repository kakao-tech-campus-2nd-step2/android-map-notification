package campus.tech.kakao.map.data.repository.log

import campus.tech.kakao.map.data.db.AppDatabase
import campus.tech.kakao.map.data.db.entity.Place
import javax.inject.Inject

class LogRepository @Inject constructor (private val placeDatabase: AppDatabase): LogRepositoryInterface {
    override suspend fun getAllLogs(): List<Place> {
        return placeDatabase.placeDao().getAllLogs()
    }

    override suspend fun haveAnyLog(): Boolean {
        return placeDatabase.placeDao().getPlaceCount() > 0
    }

    override suspend fun insertLog(place: Place) {
        placeDatabase.placeDao().insertLog(place)
    }

    override suspend fun deleteLog(place: Place) {
        placeDatabase.placeDao().deleteLog(place)
    }
}