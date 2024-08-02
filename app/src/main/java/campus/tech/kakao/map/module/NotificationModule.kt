package campus.tech.kakao.map.module

import campus.tech.kakao.map.Notification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
	@Provides
	@Singleton
	fun provideNotification(): Notification {
		return Notification()
	}
}