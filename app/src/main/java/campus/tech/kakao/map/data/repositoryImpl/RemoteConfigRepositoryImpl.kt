package campus.tech.kakao.map.data.repositoryImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    override fun getConfig(key: String): LiveData<String> {
        val data = MutableLiveData<String>()

        // Set initial value from local cache
        data.value = firebaseRemoteConfig.getString(key)

        // Fetch and activate the remote config
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update LiveData with the new value
                    data.value = firebaseRemoteConfig.getString(key)
                    Log.d("RemoteConfig", "Fetched value: ${data.value}")
                } else {
                    // Handle the error
                    Log.e("RemoteConfig", "Fetch failed", task.exception)
                    data.value = "Failure"  // Optional: set error value
                }
            }

        return data
    }
}
