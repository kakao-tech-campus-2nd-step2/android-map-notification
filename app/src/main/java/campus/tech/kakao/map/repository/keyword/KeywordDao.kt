package campus.tech.kakao.map.repository.keyword

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.repository.keyword.KeywordContract.RECENT_KEYWORD
import campus.tech.kakao.map.repository.keyword.KeywordContract.TABLE_NAME

@Dao
interface KeywordDao {
    @Query("SELECT recent_keyword FROM keyword")
    suspend fun read(): List<String>

    @Insert
    suspend fun update(keyword: Keyword)

    @Query("DELETE FROM $TABLE_NAME WHERE $RECENT_KEYWORD = :keyword")
    suspend fun delete(keyword: String)
}
