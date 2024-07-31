package campus.tech.kakao.map.data

import android.util.Log
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.domain.model.ConfigData
import campus.tech.kakao.map.domain.repository.ConfigRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteConfigRepository
@Inject
constructor(val remoteConfig: FirebaseRemoteConfig): ConfigRepository {

    override fun getData(): ConfigData {
        return ConfigData(
            serviceState = remoteConfig.getString("serviceState"),
            serviceMessage = remoteConfig.getString("serviceMessage")
        )
    }

    suspend fun getFetchedRemoteResult(): Result<ConfigData> {
        return withContext(Dispatchers.IO){
            try {
                remoteConfig.fetchAndActivate().await()
                Result.success(getData())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}