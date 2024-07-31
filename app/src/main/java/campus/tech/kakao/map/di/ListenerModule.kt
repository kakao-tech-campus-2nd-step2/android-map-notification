package campus.tech.kakao.map.di

import android.app.Activity
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.search.SearchActivity
import campus.tech.kakao.map.presentation.search.SearchActivityListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ListenerModule {

    @Provides
    @ActivityScoped
    fun provideSearchActivityListener(activity: Activity): SearchActivityListener{
        val searchActivity = activity as SearchActivity

        return object : SearchActivityListener{
            override fun onPlaceClick(place: Place) {
                searchActivity.handlePlaceClick(place)
            }

            override fun onLogDelBtnClick(logId: String) {
                searchActivity.handleLogDelBtnClick(logId)
            }
        }
    }
}