package campus.tech.kakao.map.data.datasource.Remote

import campus.tech.kakao.map.data.datasource.Local.Entity.ConfigEntity

interface ConfigService {
    suspend fun getConfig() : ConfigEntity
}