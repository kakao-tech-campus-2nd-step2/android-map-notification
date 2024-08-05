package campus.tech.kakao.map.viewmodel

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.ktx.Firebase

class RemoteConfigManager {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        remoteConfig.setDefaultsAsync(
            mapOf(
                "serviceState" to "OFF_SERVICE",
                "serviceMessage" to "서버 점검 중입니다."
            )
        )
        remoteConfig.fetchAndActivate()
    }

    val serviceState: String
        get() = remoteConfig.getString("serviceState")

    val serviceMessage: String
        get() = remoteConfig.getString("serviceMessage")
}
