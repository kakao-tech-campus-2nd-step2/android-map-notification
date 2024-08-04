package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferenceManager(context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideAppStatusNotificationHelper(@ApplicationContext context: Context): AppStatusNotificationHelper {
        return AppStatusNotificationHelper(context)
    }
}

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideNotificationPermissionManager(
        @ActivityContext context: Context,
        notificationHelper: AppStatusNotificationHelper
    ): MapNotificationManager {
        return MapNotificationManager(context, notificationHelper)
    }
}

