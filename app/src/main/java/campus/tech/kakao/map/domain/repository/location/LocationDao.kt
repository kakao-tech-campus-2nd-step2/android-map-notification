package campus.tech.kakao.map.domain.repository.location

import androidx.room.Dao
import androidx.room.Query
import campus.tech.kakao.map.data.entity.LocationEntity

@Dao
interface LocationDao {
    @Query("SELECT * FROM location WHERE category_group_name = :category")
    suspend fun searchByCategory(category: String): List<LocationEntity>
}
