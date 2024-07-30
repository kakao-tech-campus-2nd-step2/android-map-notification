package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.FirebaseRemoteConfigDomain

interface FirebaseRemoteConfigRepository {
    suspend fun fetchRemoteConfig(): Boolean
    fun getRemoteConfig(): FirebaseRemoteConfigDomain
}
