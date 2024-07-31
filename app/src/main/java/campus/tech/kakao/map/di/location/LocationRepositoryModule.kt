package campus.tech.kakao.map.di.location

import campus.tech.kakao.map.network.KakaoLocalService
import campus.tech.kakao.map.repository.location.KakaoLocationImpl
import campus.tech.kakao.map.repository.location.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocationRepositoryModule {
    @Singleton
    @Provides
    fun provideLocationRepository(kakaoLocalService: KakaoLocalService): LocationRepository {
        return KakaoLocationImpl(kakaoLocalService)
    }
}
