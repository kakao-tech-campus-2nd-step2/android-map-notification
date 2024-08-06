package campus.tech.kakao.map.di.map

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedPreferenceModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("LAST_POSITION", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext
}