package campus.tech.kakao.map.module

import campus.tech.kakao.map.data.mapPosition.MapPositionPreferences
import campus.tech.kakao.map.data.searchWord.SearchWordDao
import campus.tech.kakao.map.repository.MapPositionRepository
import campus.tech.kakao.map.repository.SearchWordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
	@Provides
	@Singleton
	fun provideSearchWordRepository(searchWordDao: SearchWordDao): SearchWordRepository {
		return SearchWordRepository(searchWordDao)
	}

	@Provides
	@Singleton
	fun provideMapPositionRepository(mapPositionPreferences: MapPositionPreferences): MapPositionRepository {
		return MapPositionRepository(mapPositionPreferences)
	}
}