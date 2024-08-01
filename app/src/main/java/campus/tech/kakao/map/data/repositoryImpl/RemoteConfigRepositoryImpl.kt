package campus.tech.kakao.map.data.repositoryImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    override suspend fun getConfig(key: String): String {
        var data = firebaseRemoteConfig.getString(key)


        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newData = firebaseRemoteConfig.getString(key)
                    data = newData
                    Log.d("RemoteConfig", "Fetch succeeded ${data}")
                } else {
                    Log.e("RemoteConfig", "Fetch failed", task.exception)
                }
            }.await()

        return data
    }
}
