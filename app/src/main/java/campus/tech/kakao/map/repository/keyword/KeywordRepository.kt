package campus.tech.kakao.map.repository.keyword

import campus.tech.kakao.map.repository.keyword.Keyword

interface KeywordRepository {
    suspend fun update(keyword: Keyword)
    suspend fun read(): List<String>
    suspend fun delete(keyword: String)
    fun close()
}
