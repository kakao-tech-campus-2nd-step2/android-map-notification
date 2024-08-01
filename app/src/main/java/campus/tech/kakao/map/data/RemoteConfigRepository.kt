package campus.tech.kakao.map.data

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.view.isVisible
import campus.tech.kakao.map.view.MapActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Flow
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) {

    suspend fun getRemoteConfig() = flow {
        val result = remoteConfig.fetchAndActivate().await()
        val state = remoteConfig.getString("service_state")
        val serviceMessage = remoteConfig.getString("service_message")
        emit(RemoteConfigs(state, serviceMessage))
    }
}