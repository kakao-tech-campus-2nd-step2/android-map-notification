package campus.tech.kakao.map.repository.splashscreen

import campus.tech.kakao.map.model.splashscreen.Service
import campus.tech.kakao.map.model.splashscreen.ServiceRemoteConfig.SERVICE_MESSAGE
import campus.tech.kakao.map.model.splashscreen.ServiceRemoteConfig.SERVICE_STATE
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val ON_SERIVCE = "ON_SERVICE"

class FirebaseRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    suspend fun getService(): Service {
        val service = Service("", "")
        remoteConfig.fetchAndActivate().await()
        service.state = remoteConfig.getString(SERVICE_STATE)
        if (service.state != ON_SERIVCE)
            service.msg = remoteConfig.getString(SERVICE_MESSAGE)
        return service
    }
}