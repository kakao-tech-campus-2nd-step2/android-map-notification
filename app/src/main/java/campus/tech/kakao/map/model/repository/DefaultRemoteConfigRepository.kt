package campus.tech.kakao.map.model.repository

import android.util.Log
import campus.tech.kakao.map.model.ServiceInfo
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

class DefaultRemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
): RemoteConfigRepository {

    override fun initConfigs() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 바로바로 가져옴
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("jieun", "Successful ${it.result}")
                } else {
                    Log.d("jieun", "Failed ${it.result}")
                }
            }.addOnFailureListener {
                Log.d("jieun", "Exception ${it.message}")
            }
    }

    override fun getServiceInfo(): ServiceInfo {
        return ServiceInfo(
            serviceState = remoteConfig.getString("serviceState"),
            serviceMessage = remoteConfig.getString("serviceMessage")
        )
    }
}