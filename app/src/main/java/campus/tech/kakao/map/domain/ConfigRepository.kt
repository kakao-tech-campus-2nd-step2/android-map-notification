package campus.tech.kakao.map.domain

import campus.tech.kakao.map.domain.vo.Config

interface ConfigRepository {
    suspend fun getConfig() : Config
}