package campus.tech.kakao.map.keyword

import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.repository.keyword.Keyword
import campus.tech.kakao.map.repository.keyword.KeywordDatabase
import campus.tech.kakao.map.repository.keyword.KeywordRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class KeywordRepositoryImplTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var keywordDatabase: KeywordDatabase

    private lateinit var keywordRepository: KeywordRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        keywordRepository = KeywordRepositoryImpl(keywordDatabase)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        keywordDatabase.close()
    }

    @Test
    fun 저장소에_키워드가_추가된다() {
        runTest {
            val keyword = Keyword(keyword = "test")

            keywordRepository.update(keyword)

            val result = keywordRepository.read()

            assertEquals(listOf("test"), result)
        }
    }

    @Test
    fun 저장소에_키워드가_삭제된다() {
        runTest {
            val keyword = Keyword(keyword = "test")

            keywordRepository.update(keyword)
            keywordRepository.delete("test")

            val result = keywordRepository.read()

            assertEquals(emptyList<String>(), result)
        }
    }
}
