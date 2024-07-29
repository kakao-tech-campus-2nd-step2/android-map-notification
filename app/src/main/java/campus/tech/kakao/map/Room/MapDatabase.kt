package campus.tech.kakao.map.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MapItemEntity::class], version = 1)
abstract class MapDatabase : RoomDatabase() {
    abstract fun selectMapItemDao(): SelectMapItemDao

}