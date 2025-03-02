package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.RemoteConfig
import campus.tech.kakao.map.model.datasource.RemoteConfigDataSource
import javax.inject.Inject

class DefaultRemoteConfigRepository @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource
): RemoteConfigRepository {
    override fun getRemoteConfig(): RemoteConfig {
        return remoteConfigDataSource.getRemoteConfig()
    }
}