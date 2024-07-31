package campus.tech.kakao.map.di.network

import campus.tech.kakao.map.network.KakaoLocalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class KakaoLocalServiceModule {
    @Singleton
    @Provides
    fun provideRetorfit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideKakaoLocalService(retrofit: Retrofit): KakaoLocalService =
        retrofit.create(KakaoLocalService::class.java)
}
