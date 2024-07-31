package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.ServiceInfo

interface RemoteConfigRepository {
    fun initConfigs()

    fun getServiceInfo(): ServiceInfo
}