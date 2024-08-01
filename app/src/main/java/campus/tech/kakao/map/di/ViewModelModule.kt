package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.DefaultFirebaseRemoteConfigRepository
import campus.tech.kakao.map.data.repository.DefaultLocationRepository
import campus.tech.kakao.map.data.repository.DefaultPlaceRepository
import campus.tech.kakao.map.data.repository.DefaultSavedSearchWordRepository
import campus.tech.kakao.map.domain.repository.FirebaseRemoteConfigRepository
import campus.tech.kakao.map.domain.repository.LocationRepository
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.repository.SavedSearchWordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPlaceRepository(
        defaultPlaceRepository: DefaultPlaceRepository,
    ): PlaceRepository

    @Binds
    @ViewModelScoped
    abstract fun bindSavedSearchWordRepository(
        defaultSavedSearchWordRepository: DefaultSavedSearchWordRepository,
    ): SavedSearchWordRepository

    @Binds
    @ViewModelScoped
    abstract fun bindLocationRepository(
        defaultLocationRepository: DefaultLocationRepository,
    ): LocationRepository

    @Binds
    @ViewModelScoped
    abstract fun bindFirebaseRemoteConfigRepository(
        defaultFirebaseRemoteConfigRepository: DefaultFirebaseRemoteConfigRepository,
    ): FirebaseRemoteConfigRepository
}
