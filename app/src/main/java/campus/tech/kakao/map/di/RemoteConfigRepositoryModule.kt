package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.RemoteConfigRepositoryImpl
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigRepositoryModule {
    @Binds
    abstract fun bindRemoteConfigRepository(
        remoteConfigRepositoryImpl: RemoteConfigRepositoryImpl
    ): RemoteConfigRepository
}