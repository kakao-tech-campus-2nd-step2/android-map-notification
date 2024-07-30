package campus.tech.kakao.map.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch

@Database(entities = [SavedSearch::class, Place::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedSearchDao(): SavedSearchDao
    abstract fun placeDao(): PlaceDao
}
