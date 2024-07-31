package campus.tech.kakao.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.runner.AndroidJUnit4
import campus.tech.kakao.map.domain.usecase.GetRemoteConfigUseCase
import campus.tech.kakao.map.presentation.viewmodel.SplashViewModel

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@Config(manifest = Config.NONE)
class SplashViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getRemoteConfigUseCase: GetRemoteConfigUseCase
    lateinit var splashViewModel: SplashViewModel

    @MockK
    private lateinit var serviceStateObserver: Observer<String>

    @MockK
    private lateinit var serviceMessageObserver: Observer<String>

    @MockK
    private lateinit var navigationEventObserver: Observer<Boolean>
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        splashViewModel = SplashViewModel(getRemoteConfigUseCase)
        splashViewModel.serviceMessage.observeForever(serviceMessageObserver)
        splashViewModel.serviceState.observeForever(serviceStateObserver)
        splashViewModel.navigationEvent.observeForever(navigationEventObserver)
    }

    @After()
    fun tearDown() {
        Dispatchers.resetMain()
        splashViewModel.serviceState.removeObserver(serviceStateObserver)
        splashViewModel.serviceMessage.removeObserver(serviceMessageObserver)
        splashViewModel.navigationEvent.removeObserver(navigationEventObserver)
    }

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {

            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }


    @Test
    fun `만약 ON_SERVICE 라면 맵 이동 이벤트 발생`() = runTest(UnconfinedTestDispatcher()) {
        every { navigationEventObserver.onChanged(any()) } answers {}
        every { serviceStateObserver.onChanged(any()) } answers {}
        every { serviceMessageObserver.onChanged(any()) } answers {}
        // Mocking 응답 설정
        coEvery { getRemoteConfigUseCase("serviceState") } returns MutableLiveData<String>("ON_SERVICE")

        var navigationEvent = splashViewModel.navigationEvent.value

        // ViewModel의 fetchRemoteConfig 호출
        runBlocking {
            splashViewModel.fetchRemoteConfig()
            advanceUntilIdle()
        }
        navigationEvent = splashViewModel.navigationEvent.getOrAwaitValue()

        assertEquals(true, navigationEvent)
    }





}