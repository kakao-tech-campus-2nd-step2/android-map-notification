package campus.tech.kakao.map.data.datasource.Remote

import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.datasource.Local.Entity.ConfigEntity
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig

class ConfigService {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
    }

    init{
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun getConfig() : ConfigEntity{
        remoteConfig.fetchAndActivate()
        return ConfigEntity(
            remoteConfig.getString("serviceState"),
            remoteConfig.getString("serviceMessage")
        )
    }

}