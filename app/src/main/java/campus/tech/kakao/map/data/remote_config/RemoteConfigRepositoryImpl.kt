package campus.tech.kakao.map.data.remote_config

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteConfigRepositoryImpl() : RemoteConfigRepository {
    private val remoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        setConfigSettingsAsync(configSettings)
    }

    override suspend fun getServiceInformation(): ServiceInformation? =
        suspendCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                var resultServiceInformation: ServiceInformation? = null

                if (task.isSuccessful) {
                    val serviceState = remoteConfig.getString("serviceState")
                    val serviceMessage = remoteConfig.getString("serviceMessage")
                    resultServiceInformation = ServiceInformation(serviceState, serviceMessage)
                }

                continuation.resume(resultServiceInformation)
            }
        }
}