package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.net.KakaoApi
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://dapi.kakao.com/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideKakaoApi(retrofit: Retrofit): KakaoApi {
        return retrofit.create(KakaoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideConfigInstance(): FirebaseRemoteConfig {
        val firebaseInstance = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }

        firebaseInstance.setConfigSettingsAsync(configSettings)

        return firebaseInstance
    }
}