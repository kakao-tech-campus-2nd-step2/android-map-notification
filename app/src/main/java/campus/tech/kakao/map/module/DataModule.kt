package campus.tech.kakao.map.module

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.mapPosition.MapPositionPreferences
import campus.tech.kakao.map.data.searchWord.SearchWordContract
import campus.tech.kakao.map.data.searchWord.SearchWordDao
import campus.tech.kakao.map.data.searchWord.SearchWordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext context: Context): SearchWordDatabase {
		return Room.databaseBuilder(context, SearchWordDatabase::class.java, SearchWordContract.DB_NAME)
			.build()
	}

	@Provides
	@Singleton
	fun provideDao(database: SearchWordDatabase): SearchWordDao {
		return database.searchWordDao()
	}

	@Provides
	@Singleton
	fun provideMapPosition(@ApplicationContext context: Context): MapPositionPreferences {
		return MapPositionPreferences(context)
	}
}