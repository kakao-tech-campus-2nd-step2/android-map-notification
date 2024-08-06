package campus.tech.kakao.map.di.keyword

import campus.tech.kakao.map.repository.keyword.KeywordDatabase
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import campus.tech.kakao.map.repository.keyword.KeywordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class KeywordRepositoryModule {
    @Singleton
    @Provides
    fun provideKeywordRepository(keywordDatabase: KeywordDatabase): KeywordRepository {
        return KeywordRepositoryImpl(keywordDatabase)
    }
}