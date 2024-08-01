package campus.tech.kakao.map.data.di

import campus.tech.kakao.map.data.datasource.Remote.ConfigService
import campus.tech.kakao.map.data.datasource.Remote.ConfigServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigServiceModule {
    @Binds
    @Singleton
    abstract fun bindConfigService(
        impl: ConfigServiceImpl
    ) : ConfigService
}