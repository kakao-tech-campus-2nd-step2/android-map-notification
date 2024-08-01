package campus.tech.kakao.map.di

import android.app.Activity
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.search.SearchActivity
import campus.tech.kakao.map.presentation.search.SearchActivityRecyclerviewListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ListenerModule {

    @Provides
    @ActivityScoped
    fun provideSearchActivityRecyclerviewListener(activity: Activity)
    : SearchActivityRecyclerviewListener {
        val searchActivity = activity as SearchActivity

        return object : SearchActivityRecyclerviewListener {
            override fun onPlaceClick(place: Place) {
                searchActivity.handlePlaceClick(place)
            }

            override fun onLogDelBtnClick(logId: String) {
                searchActivity.handleLogDelBtnClick(logId)
            }
        }
    }
}