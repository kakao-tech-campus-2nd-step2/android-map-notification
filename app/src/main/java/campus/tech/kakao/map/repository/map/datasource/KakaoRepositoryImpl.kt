package campus.tech.kakao.map.repository.map

import campus.tech.kakao.map.repository.map.datasource.MapDataSource
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(private val dataSource: MapDataSource) : MapRepository {
    override fun updateLastPosition(latitude: Double, longitude: Double) {
        dataSource.updateLastPosition(latitude, longitude)
    }

    override fun readLastPosition(): Pair<Double?, Double?> {
        return dataSource.readLastPosition()
    }
}