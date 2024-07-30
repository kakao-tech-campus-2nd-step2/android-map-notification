package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.data.model.FirebaseRemoteConfigData
import campus.tech.kakao.map.domain.model.FirebaseRemoteConfigDomain

fun FirebaseRemoteConfigData.map(): FirebaseRemoteConfigDomain {
    return FirebaseRemoteConfigDomain(
        serviceState = this.serviceState,
        serviceMessage = this.serviceMessage,
    )
}
