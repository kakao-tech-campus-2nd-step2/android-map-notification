package campus.tech.kakao.map.data.repository

import android.content.Intent
import android.util.Log
import campus.tech.kakao.map.domain.model.RemoteConfig
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import campus.tech.kakao.map.presentation.MapActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {
    private lateinit var remoteConfig: RemoteConfig

    override suspend fun fetchAndActivateRemoteConfig(): RemoteConfig {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val serviceState = getServiceState()
                val serviceMessage = getServiceMessage()
                remoteConfig = RemoteConfig(serviceState, serviceMessage)
            }
        }.await()
        return RemoteConfig(getServiceState(), getServiceMessage())
    }

    private fun getServiceState(): String = firebaseRemoteConfig.getString(RemoteConfig.KEY_SERVICE_STATE)
    private fun getServiceMessage(): String = firebaseRemoteConfig.getString(RemoteConfig.KEY_SERVICE_MESSAGE)
}