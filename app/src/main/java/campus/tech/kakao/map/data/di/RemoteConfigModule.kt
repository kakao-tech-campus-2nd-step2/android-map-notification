package campus.tech.kakao.map.data.di

import campus.tech.kakao.map.repository.RemoteConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Provides
    @Singleton
    fun provideRemoteConfigManager(): RemoteConfigManager {
        RemoteConfigManager.init()
        return RemoteConfigManager
    }
}