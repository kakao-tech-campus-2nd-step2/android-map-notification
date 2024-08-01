package campus.tech.kakao.map.di

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
class firebaseModule {
    @Singleton
    @Provides
    fun provideRemoteConfig() : FirebaseRemoteConfig {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        val firebase = Firebase.remoteConfig
        firebase.setConfigSettingsAsync(configSettings)
        return firebase
    }
}