package campus.tech.kakao.map.data.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.data.model.DBPlace
import campus.tech.kakao.map.data.model.DBPlace.Companion.TABLE_NAME

@Dao
interface PlaceDao {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllPlaces(): List<DBPlace>

    @Insert
    suspend fun insertAll(vararg dbPlace: DBPlace) {
        Log.d("search2", "insetAll: ${Thread.currentThread().name}")
        // 워커 스레드에서 실행됨
    }

    @Delete
    suspend fun delete(dbPlace: DBPlace)

}