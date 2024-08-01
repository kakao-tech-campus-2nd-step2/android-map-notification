package campus.tech.kakao.map.domain.repository

import androidx.lifecycle.LiveData

interface RemoteConfigRepository {
    suspend fun getConfig(key: String): String
}