package campus.tech.kakao.map.model.network

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class RemoteConfig {

    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private var state = "초기값"
    var message = "초기값"


    init{
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 개발용
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    suspend fun fetchRemoteConfigState() : String {
        return try {
            Tasks.await(remoteConfig.fetchAndActivate())
            state = remoteConfig.getString("serviceState")
            Log.d("seyoung", "remoteConfig-remoteConfigState : $state")
            state
        } catch (e: Exception) {
            Log.d("seyoung", "remoteConfig-fetch 실패")
            state
        }
    }

    suspend fun fetchRemoteConfigMessage() : String {
        return try {
            Tasks.await(remoteConfig.fetchAndActivate())
            message = remoteConfig.getString("serviceMessage")
            Log.d("seyoung", "remoteConfig-remoteCongiMessage : $message")
            message
        } catch (e: Exception) {
            Log.d("seyoung", "remoteConfig-fetch 실패")
            message
        }
    }

}