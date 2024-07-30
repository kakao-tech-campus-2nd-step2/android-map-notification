package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.mapper.map
import campus.tech.kakao.map.data.model.FirebaseRemoteConfigData
import campus.tech.kakao.map.domain.model.FirebaseRemoteConfigDomain
import campus.tech.kakao.map.domain.repository.FirebaseRemoteConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultFirebaseRemoteConfigRepository @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : FirebaseRemoteConfigRepository {

    override suspend fun fetchRemoteConfig(): Boolean {
        return try {
            firebaseRemoteConfig.fetchAndActivate().await()
            true
        } catch (e: Exception) {
            Log.e("DefaultFirebaseRemoteConfigRepo", "Error fetching remote config", e)
            false
        }
    }

    override fun getRemoteConfig(): FirebaseRemoteConfigDomain {
        return FirebaseRemoteConfigData(
            serviceState = firebaseRemoteConfig.getString(SERVICE_STATE),
            serviceMessage = firebaseRemoteConfig.getString(SERVICE_MESSAGE),
        ).map()
    }

    companion object ConfigKeys {
        const val SERVICE_STATE = "serviceState"
        const val ON_SERVICE = "ON_SERVICE"
        const val SERVICE_MESSAGE = "serviceMessage"
    }
}
