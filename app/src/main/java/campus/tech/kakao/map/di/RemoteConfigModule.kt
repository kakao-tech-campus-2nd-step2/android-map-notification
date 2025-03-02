package campus.tech.kakao.map.di

import android.util.Log
import campus.tech.kakao.map.model.datasource.RemoteConfigDataSource
import campus.tech.kakao.map.model.repository.RemoteConfigRepository
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig
        return initRemoteConfig(remoteConfig)
    }

    fun initRemoteConfig(remoteConfig: FirebaseRemoteConfig): FirebaseRemoteConfig {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 바로바로 가져옴
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("jieun", "Successful ${it.result}")
                } else {
                    Log.d("jieun", "Failed ${it.result}")
                }
            }.addOnFailureListener {
                Log.d("jieun", "Exception ${it.message}")
            }
        return remoteConfig
    }

    @Singleton
    @Provides
    fun providesRemoteConfigDataSource(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfigDataSource {
        return RemoteConfigDataSource(firebaseRemoteConfig)
    }



}