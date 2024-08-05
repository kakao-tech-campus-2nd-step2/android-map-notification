package campus.tech.kakao.map.di.map

import campus.tech.kakao.map.repository.map.MapRepository
import campus.tech.kakao.map.repository.map.MapRepositoryImpl
import campus.tech.kakao.map.repository.map.datasource.MapDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MapRepositoryModule {
    @Singleton
    @Provides
    fun provideMapRepository(dataSource: MapDataSource): MapRepository {
        return MapRepositoryImpl(dataSource)
    }
}