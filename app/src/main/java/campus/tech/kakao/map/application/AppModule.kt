package campus.tech.kakao.map.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.database.PlacesRoomDB
import campus.tech.kakao.map.data.network.api.RetrofitService
import campus.tech.kakao.map.data.remote.ConfigService
import campus.tech.kakao.map.data.repository.DataStoreManager
import campus.tech.kakao.map.data.repository.MapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Provides
    @Singleton
    fun provideMapRepository(
        @ApplicationContext context: Context,
        configService: ConfigService,
        retrofitService: RetrofitService,
        dataStoreManager: DataStoreManager,
        placeDao: PlaceDao,
        placesRoomDB: PlacesRoomDB
    ): MapRepository {
        return MapRepository(context, configService, retrofitService, dataStoreManager, placeDao, placesRoomDB)
    }

    @Provides
    @Singleton
    fun providePlaceDao(placesRoomDB: PlacesRoomDB): PlaceDao {
        return placesRoomDB.placeDao()
    }

    @Provides
    @Singleton
    fun providePlacesDatabase(@ApplicationContext context: Context): PlacesRoomDB {
        return PlacesRoomDB.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return DataStoreManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideConfigService(): ConfigService {
        return ConfigService()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
    }

}