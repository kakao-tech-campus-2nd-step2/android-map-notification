package campus.tech.kakao.map.data.di

import android.content.SharedPreferences
import campus.tech.kakao.map.data.ConfigRepositoryImpl
import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.data.datasource.Local.DB.RoomDB
import campus.tech.kakao.map.data.datasource.Remote.ConfigService
import campus.tech.kakao.map.data.datasource.Remote.RemoteService
import campus.tech.kakao.map.data.datasource.Remote.RetrofitService
import campus.tech.kakao.map.domain.ConfigRepository
import campus.tech.kakao.map.domain.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun provideConfigRepository(
        configService: ConfigService
    ) : ConfigRepository =
        ConfigRepositoryImpl(configService)

    @Provides
    @Singleton
    fun provideConfigService() : ConfigService = ConfigService()
}