package campus.tech.kakao.map.data.di

import campus.tech.kakao.map.data.ConfigRepositoryImpl
import campus.tech.kakao.map.data.datasource.Remote.ConfigService
import campus.tech.kakao.map.data.datasource.Remote.ConfigServiceImpl
import campus.tech.kakao.map.domain.ConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {
    @Binds
    @Singleton
    abstract fun bindConfigRepository(
        impl: ConfigRepositoryImpl
    ) : ConfigRepository

    @Binds
    @Singleton
    abstract fun bindConfigService(
        impl: ConfigServiceImpl
    ) : ConfigService
}