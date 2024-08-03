package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.db.entity.Place

interface LogRepositoryInterface {
    suspend fun getAllLogs(): List<Place>
    suspend fun haveAnyLog(): Boolean
    suspend fun insertLog(place: Place)
    suspend fun deleteLog(place: Place)
}