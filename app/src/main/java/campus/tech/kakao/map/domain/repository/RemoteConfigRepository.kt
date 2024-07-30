package campus.tech.kakao.map.domain.repository

import androidx.lifecycle.LiveData

interface RemoteConfigRepository {
    fun getConfig(key: String): LiveData<String>
}