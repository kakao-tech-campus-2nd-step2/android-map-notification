package campus.tech.kakao.map.data.di

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.data.db.AppDatabase
import campus.tech.kakao.map.data.remote.api.KakaoApiService
import campus.tech.kakao.map.data.repository.log.LogRepository
import campus.tech.kakao.map.data.repository.log.LogRepositoryInterface
import campus.tech.kakao.map.data.repository.map.MapRepository
import campus.tech.kakao.map.data.repository.map.MapRepositoryInterface
import campus.tech.kakao.map.data.repository.place.PlaceRepository
import campus.tech.kakao.map.data.repository.place.PlaceRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    @Singleton
    fun providePlaceRepository(sharedPreferences: SharedPreferences, kakaoApiService: KakaoApiService): PlaceRepositoryInterface {
        return PlaceRepository(sharedPreferences, kakaoApiService)
    }

    @Provides
    @Singleton
    fun provideLogRepository(placeDatabase: AppDatabase): LogRepositoryInterface {
        return LogRepository(placeDatabase)
    }

    @Provides
    @Singleton
    fun provideMapRepository(sharedPreferences: SharedPreferences): MapRepositoryInterface {
        return MapRepository(sharedPreferences)
    }
}
