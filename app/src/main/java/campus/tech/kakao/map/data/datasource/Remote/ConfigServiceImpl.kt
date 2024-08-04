package campus.tech.kakao.map.data.datasource.Remote

import campus.tech.kakao.map.data.datasource.Local.Entity.ConfigEntity
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigServiceImpl @Inject constructor() : ConfigService {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
    }

    init{
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    override suspend fun getConfig() : ConfigEntity = withContext(Dispatchers.IO){
        remoteConfig.fetchAndActivate().await()
        ConfigEntity(
            remoteConfig.getString("serviceState"),
            remoteConfig.getString("serviceMessage")
        )
    }

}