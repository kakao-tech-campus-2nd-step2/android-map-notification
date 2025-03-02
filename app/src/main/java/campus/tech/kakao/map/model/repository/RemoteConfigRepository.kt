package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.RemoteConfig


interface RemoteConfigRepository {
    fun getRemoteConfig(): RemoteConfig
}