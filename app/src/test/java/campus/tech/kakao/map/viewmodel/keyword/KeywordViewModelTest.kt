package campus.tech.kakao.map.viewmodel.keyword

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.repository.keyword.Keyword
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import campus.tech.kakao.map.viewmodel.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class KeywordViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var keywordRepository: KeywordRepository
    private lateinit var viewModel: KeywordViewModel

    @Before
    fun setUp() {
        keywordRepository = mockk(relaxed = true)
        viewModel = KeywordViewModel(keywordRepository)
    }

    @Test
    fun `키워드 히스토리를 가져온다`() =
        runTest {
        coEvery { keywordRepository.read() } returns listOf("카페", "약국")
        viewModel.readKeywordHistory()
        assertEquals(listOf("카페", "약국"), viewModel.keyword.value)
    }

    @Test
    fun `검색 결과 목록 중 카페를 선택했을 때 키워드 히스토리가 업데이트 된다`() =
        runTest {
        // given
        val keyword = "카페"
        coEvery { keywordRepository.read() } returns listOf(keyword)
        val localUiModel =
            mockk<LocalUiModel> {
            every { place } returns keyword
        }

        // when
        viewModel.onSearchItemClick(localUiModel)

        // then
        coVerify { keywordRepository.delete(keyword) }
        coVerify { keywordRepository.update(Keyword(keyword = keyword)) }
        assertEquals(listOf(keyword), viewModel.keyword.value)
    }

    @Test
    fun `키워드 히스토리 목록 중 카페를 제거했을 때 해당 키워드가 제거된다`() =
        runTest {
            // given
            val keyword = "약국"
            coEvery { keywordRepository.read() } returns listOf(keyword)

            // when
            viewModel.onKeywordItemDeleteClick(keyword)

            coVerify { keywordRepository.delete(keyword) }
        }

    @Test
    fun `키워드 히스토리 목록 중 카페를 선택했을 때 키워드 히스토리가 업데이트 된다`() =
        runTest {
            // given
            val keyword = "카페"
            coEvery { keywordRepository.read() } returns listOf(keyword)

            // when
            viewModel.onKeywordItemClick(keyword)

            // then
            assertEquals(keyword, viewModel.keywordClicked.value)
            coVerify { keywordRepository.delete(keyword) }
            coVerify { keywordRepository.update(Keyword(keyword = keyword)) }
            assertEquals(listOf(keyword), viewModel.keyword.value)
        }
}
