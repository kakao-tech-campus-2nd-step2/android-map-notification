package campus.tech.kakao.map.model.datasource

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.RemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class RemoteConfigDataSource @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
){
    fun getRemoteConfig(): RemoteConfig {
        return RemoteConfig(
            serviceState = firebaseRemoteConfig.getString(BuildConfig.SERVICE_STATE),
            serviceMessage = firebaseRemoteConfig.getString(BuildConfig.SERVICE_MESSAGE)
        )
    }
}