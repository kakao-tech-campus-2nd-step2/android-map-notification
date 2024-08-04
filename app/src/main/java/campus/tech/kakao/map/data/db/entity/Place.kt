package campus.tech.kakao.map.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "research")
data class Place (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val location: String,
    val category: String,
    val x: String,
    val y: String
)