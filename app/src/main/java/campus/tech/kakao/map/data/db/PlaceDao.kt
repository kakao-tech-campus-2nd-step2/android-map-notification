package campus.tech.kakao.map.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.data.db.entity.Place

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLog(place: Place)

    @Delete
    suspend fun deleteLog(place: Place)

    @Query("SELECT * FROM research")
    suspend fun getAllLogs(): List<Place>

    @Query("SELECT COUNT(*) FROM research")
    suspend fun getPlaceCount(): Int
}