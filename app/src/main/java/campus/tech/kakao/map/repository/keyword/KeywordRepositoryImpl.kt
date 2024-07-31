package campus.tech.kakao.map.repository.keyword

import javax.inject.Inject

class KeywordRepositoryImpl @Inject constructor(private val keywordDatabase: KeywordDatabase) :
    KeywordRepository {
    override suspend fun update(keyword: Keyword) {
        keywordDatabase.keywordDao().update(keyword)
    }

    override suspend fun read(): List<String> {
        return keywordDatabase.keywordDao().read()
    }

    override suspend fun delete(keyword: String) {
        keywordDatabase.keywordDao().delete(keyword)
    }

    override fun close() {
        keywordDatabase.close()
    }
}
