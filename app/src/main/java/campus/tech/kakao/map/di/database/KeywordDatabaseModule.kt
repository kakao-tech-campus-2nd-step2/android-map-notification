package campus.tech.kakao.map.di.database

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.repository.keyword.KeywordDao
import campus.tech.kakao.map.repository.keyword.KeywordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "keyword"

@InstallIn(SingletonComponent::class)
@Module
class KeywordDatabaseModule {
    @Singleton
    @Provides
    fun provideKeywordDatabase(@ApplicationContext appContext: Context): KeywordDatabase {
        return Room.databaseBuilder(
            appContext,
            KeywordDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideKeywordDao(database: KeywordDatabase): KeywordDao {
        return database.keywordDao()
    }
}
