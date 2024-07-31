package campus.tech.kakao.map.data.di

import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaceRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ) : PlaceRepository
}