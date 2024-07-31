package campus.tech.kakao.map.di

import campus.tech.kakao.map.model.repository.DefaultRemoteConfigRepository
import campus.tech.kakao.map.model.repository.RemoteConfigRepository
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
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
        return Firebase.remoteConfig
    }

    @Singleton
    @Provides
    fun providesDefaultRemoteConfigRepository(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfigRepository {
        return DefaultRemoteConfigRepository(firebaseRemoteConfig)
    }
}