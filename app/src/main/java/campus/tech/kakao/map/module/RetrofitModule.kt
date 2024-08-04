package campus.tech.kakao.map.module

import campus.tech.kakao.map.data.remote.RetrofitService
import campus.tech.kakao.map.data.remote.UrlContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

	@Provides
	@Singleton
	fun provideRetrofitService(retrofit: Retrofit): RetrofitService {
		return retrofit.create(RetrofitService::class.java)
	}

	@Provides
	@Singleton
	fun provideRetrofit():Retrofit {
		return Retrofit.Builder()
			.baseUrl(UrlContract.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}
}