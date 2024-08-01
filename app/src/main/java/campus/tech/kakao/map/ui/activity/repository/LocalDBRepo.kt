package campus.tech.kakao.map.ui.activity.repository

import campus.tech.kakao.map.data.model.DBPlace

interface LocalDBRepo {

    suspend fun getAllPlaces(): List<DBPlace>

    suspend fun insertAll(vararg dbPlace: DBPlace)

    suspend fun delete(dbPlace: DBPlace)

}