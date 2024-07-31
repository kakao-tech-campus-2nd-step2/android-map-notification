package campus.tech.kakao.map.repository.keyword

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class KeywordDaoTest {
    @Inject
    lateinit var keywordDao: KeywordDao
    private lateinit var context: Context

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject()
    }

    @Test
    fun `카페 키워드가 저장되어 있으면 저장된 키워드를 불러올 수 있다`() {
        runTest {
            // given
            val keyword = "카페"
            keywordDao.update(Keyword(keyword = keyword))

            // when
            val keywords = keywordDao.read()

            // then
            assertEquals(listOf(keyword), keywords)
        }
    }

    @Test
    fun `저장된 키워드인 약국을 삭제한다`() {
        runTest {
            // given
            val keyword = "약국"
            keywordDao.update(Keyword(keyword = keyword))

            // when
            keywordDao.delete(keyword)

            // then
            val keywords = keywordDao.read()
            assertEquals(emptyList<String>(), keywords)
        }
    }
}

