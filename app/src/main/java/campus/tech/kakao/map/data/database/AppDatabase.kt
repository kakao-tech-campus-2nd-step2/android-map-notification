package campus.tech.kakao.map.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.data.entity.KeywordEntity
import campus.tech.kakao.map.data.entity.LocationEntity
import campus.tech.kakao.map.domain.repository.keyword.KeywordDao
import campus.tech.kakao.map.domain.repository.location.LocationDao

@Database(entities = [KeywordEntity::class, LocationEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
    abstract fun locationDao(): LocationDao
}