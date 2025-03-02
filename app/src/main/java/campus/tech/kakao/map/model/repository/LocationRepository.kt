package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location

interface LocationRepository {
    suspend fun getLocationAll(query: String, page: Int): DefaultLocationRepository.LocationsInfo
    fun addLastLocation(location: Location)
    fun getLastLocation(): Location?
}