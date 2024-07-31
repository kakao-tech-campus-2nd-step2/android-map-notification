package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.RemoteConfig

interface RemoteConfigRepository {
    suspend fun fetchAndActivateRemoteConfig(): RemoteConfig
}