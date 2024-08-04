package campus.tech.kakao.map.data.repository.place

import campus.tech.kakao.map.data.db.entity.Place

interface PlaceRepositoryInterface {
    suspend fun searchPlaces(query: String): List<Place>

    fun saveLastLocation(item: Place)
}