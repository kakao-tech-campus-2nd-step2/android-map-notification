package campus.tech.kakao.map.di.map

import android.content.SharedPreferences
import campus.tech.kakao.map.repository.map.datasource.MapDataSource
import campus.tech.kakao.map.repository.map.datasource.MapDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MapDataSourceModule {
    @Singleton
    @Provides
    fun provideMapDataSource(preference: SharedPreferences): MapDataSource {
        return MapDataSourceImpl(preference)
    }
}