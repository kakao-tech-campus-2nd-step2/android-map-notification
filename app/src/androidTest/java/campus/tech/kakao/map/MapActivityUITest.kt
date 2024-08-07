package campus.tech.kakao.map

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.presentation.map.MapActivity
import campus.tech.kakao.map.presentation.search.SearchActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityUITest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MapActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun `검색_버튼_클릭_시_검색_화면으로_이동`() {

        // 자동 호출 된 bottom sheet 닫기
        closeBottomSheet()

        //When
        onView(withId(R.id.btnSearch))
            .check(matches(isDisplayed()))
            .perform(click())

        //Then
        Intents.intended(hasComponent(SearchActivity::class.java.name))
    }

    private fun closeBottomSheet() {
        onView(withId(R.id.bottom_sheet))
            .perform(click())
    }
}
