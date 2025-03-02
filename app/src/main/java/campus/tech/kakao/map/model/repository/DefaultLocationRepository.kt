package campus.tech.kakao.map.model.repository

import android.util.Log
import campus.tech.kakao.map.model.CountDto
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Location.Companion.toLocation
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.model.datasource.LastLocationSharedPreferences
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLocationRepository @Inject constructor(
    private val locationRemoteDataSource: LocationRemoteDataSource,
    private val lastLocationSharedPreferences: LastLocationSharedPreferences
): LocationRepository {
    override suspend fun getLocationAll(query: String, page: Int): LocationsInfo {
        val searchFromKeywordResponse = locationRemoteDataSource.getLocations(query, page)
        Log.d("jieun", "searchFromKeywordResponse:" +searchFromKeywordResponse.toString())
        val locationDtos: List<LocationDto> = searchFromKeywordResponse?.documents ?: emptyList()
        val countDto: CountDto = searchFromKeywordResponse?.meta ?: CountDto(-1, -1, true)
        return LocationsInfo(toLocations(locationDtos), countDto.isEnd)
    }

    private fun toLocations(locationDtos: List<LocationDto>) =
        locationDtos.map {
            it.toLocation()
        }
    override fun addLastLocation(location: Location){
        lastLocationSharedPreferences.putLastLocation(location)
    }

    override fun getLastLocation(): Location? {
        return lastLocationSharedPreferences.getLastLocation()
    }
    data class LocationsInfo(val location:List<Location>, val isEnd:Boolean)
}