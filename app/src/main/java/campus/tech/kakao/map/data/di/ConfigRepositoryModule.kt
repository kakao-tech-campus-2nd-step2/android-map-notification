package campus.tech.kakao.map.data.di

import campus.tech.kakao.map.data.ConfigRepositoryImpl
import campus.tech.kakao.map.domain.ConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class ConfigRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindConfigRepository(
        impl: ConfigRepositoryImpl
    ) : ConfigRepository
}