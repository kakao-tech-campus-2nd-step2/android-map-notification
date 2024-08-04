package campus.tech.kakao.map.utils

import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {
    @Provides
    @Singleton
    fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()
}