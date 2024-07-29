package campus.tech.kakao.map.repository

import android.content.Context
import campus.tech.kakao.map.database.MapItemDao
import campus.tech.kakao.map.model.MapItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapItemRepository @Inject constructor(
    private val mapItemDao: MapItemDao,
    private val context: Context) {

    suspend fun insertAll(mapItems: List<MapItemEntity>) {
        mapItemDao.insertAll(mapItems)
    }

    suspend fun getAllMapItems(): List<MapItemEntity> {
        return mapItemDao.getAllMapItems()
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            mapItemDao.deleteAll()
        }
    }

    suspend fun loadKeywords(): List<String> {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("keywords", Context.MODE_PRIVATE)
            val keywordsSet = sharedPreferences.getStringSet("keywords", setOf()) ?: setOf()
            keywordsSet.toList()
        }
    }

    suspend fun saveKeywords(keywords: List<String>) {
        withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("keywords", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putStringSet("keywords", keywords.toSet())
                apply()
            }
        }
    }
}