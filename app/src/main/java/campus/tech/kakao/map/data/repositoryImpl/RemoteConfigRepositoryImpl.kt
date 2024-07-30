package campus.tech.kakao.map.data.repositoryImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    override fun getConfig(key: String): LiveData<String> {
        val data = MutableLiveData<String>()


        data.value = firebaseRemoteConfig.getString(key)


        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newData = firebaseRemoteConfig.getString(key)
                    data.value = newData
                    Log.d("RemoteConfig", "Fetch succeeded ${data.value}")
                } else {
                    // Handle the error
                    Log.e("RemoteConfig", "Fetch failed", task.exception)
                    data.value = "Failure"
                }
            }

        return data
    }
}
