package campus.tech.kakao.map.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.data.db.entity.Place

@Database(entities = [Place::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}