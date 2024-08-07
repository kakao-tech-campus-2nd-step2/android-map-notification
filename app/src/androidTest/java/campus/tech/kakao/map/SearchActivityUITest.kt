package campus.tech.kakao.map

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.data.LastVisitedPlaceManager
import campus.tech.kakao.map.data.PlaceLocalDataRepository
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.adapter.LogAdapter
import campus.tech.kakao.map.presentation.adapter.SearchedPlaceAdapter
import campus.tech.kakao.map.presentation.map.MapActivity
import campus.tech.kakao.map.presentation.search.SearchActivity
import campus.tech.kakao.map.presentation.search.SearchViewModel
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.regex.Matcher

class SearchActivityUITest {
    private lateinit var lastVisitedPlaceManager: LastVisitedPlaceManager
    private lateinit var placeDao: PlaceDao
    private lateinit var placeLocalDataRepository: PlaceLocalDataRepository

    @get:Rule
    val activityRule = ActivityScenarioRule(SearchActivity::class.java)

    @Before
    fun setUp() {
        lastVisitedPlaceManager = mockk()
        placeDao = mockk()
        placeLocalDataRepository = PlaceLocalDataRepository(placeDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `장소_리스트_클릭_시_검색_화면_종료`(){
        //Given
        onView(withId(R.id.edtSearch)).perform(replaceText("부산대학교"))

        //debounce 기능 테스트 (0.5초 후 api를 가져옴)
        Thread.sleep(3000L)

        //When
        onView(withId(R.id.recyclerPlace))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))

        //Then
        assertTrue(activityRule.scenario.state.isAtLeast(androidx.lifecycle.Lifecycle.State.DESTROYED))
    }
    

    private fun setLogMockData(){
        activityRule.scenario.onActivity {
            it.findViewById<RecyclerView>(R.id.recyclerLog)?.let {
                val places = listOf(
                    Place("1", "장소 A", "주소 A", "카테고리 A", "0.0","0.0"),
                    Place("2", "장소 B", "주소 B", "카테고리 B", "0.0","0.0"),
                )

                (it.adapter as LogAdapter).submitList(places)
            }
        }
    }


}