package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.model.DBPlace

interface LocalDBRepoImpl {

    suspend fun getAllPlaces(): List<DBPlace>

    suspend fun insertAll(vararg dbPlace: DBPlace)

    suspend fun delete(dbPlace: DBPlace)

}