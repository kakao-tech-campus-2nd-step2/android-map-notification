package campus.tech.kakao.map.repository.keyword

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.repository.keyword.Keyword
import campus.tech.kakao.map.repository.keyword.KeywordDao

@Database(entities = [Keyword::class], version = 1)
abstract class KeywordDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
}
