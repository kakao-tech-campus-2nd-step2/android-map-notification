package campus.tech.kakao.map.data.di

import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlaceRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ) : PlaceRepository
}