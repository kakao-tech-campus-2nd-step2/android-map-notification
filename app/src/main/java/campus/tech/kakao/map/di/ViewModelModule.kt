package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.data.*
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.net.KakaoApi
import campus.tech.kakao.map.domain.repository.ConfigRepository
import campus.tech.kakao.map.domain.repository.PlaceRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    // SearchViewModel
    @Singleton
    @Provides
    fun providePlaceRepository(
        @ApplicationContext context: Context,
        placeDao: PlaceDao,
        kakaoApi: KakaoApi
    ): PlaceRepository {
        return if (PlaceApplication.isNetworkActive(context)) {
            PlaceRemoteDataRepository(placeDao,kakaoApi)
        } else {
            PlaceLocalDataRepository(placeDao)
        }
    }

    // MapViewModel
    @Singleton
    @Provides
    fun provideLastVisitedPlaceManager(sharedPreferences: SharedPreferences): LastVisitedPlaceManager{
        return LastVisitedPlaceManager(sharedPreferences)
    }

    // SearchViewModel
    @Singleton
    @Provides
    fun provideRemoteConfigRepository(remoteConfig: FirebaseRemoteConfig):RemoteConfigRepository{
        return RemoteConfigRepository(remoteConfig)
    }
}
