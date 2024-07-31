package campus.tech.kakao.map.repository.keyword

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import campus.tech.kakao.map.repository.keyword.KeywordContract.RECENT_KEYWORD
import campus.tech.kakao.map.repository.keyword.KeywordContract.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Keyword(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = RECENT_KEYWORD) val keyword: String
)
