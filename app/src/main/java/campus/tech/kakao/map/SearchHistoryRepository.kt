package campus.tech.kakao.map

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepository @Inject constructor(
    private val dao: SearchHistoryDao
) {
    fun getAllSearchHistories(): Flow<List<SearchHistory>> {
        return dao.getAllHistories()
    }

    suspend fun insert(searchHistory: SearchHistory) {
        dao.insert(searchHistory)
    }

    suspend fun delete(searchHistory: SearchHistory) {
        dao.delete(searchHistory)
    }

    suspend fun update(searchHistory: SearchHistory) {
        dao.update(searchHistory)
    }
}
