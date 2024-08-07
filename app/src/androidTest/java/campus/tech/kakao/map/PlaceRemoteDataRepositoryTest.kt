package campus.tech.kakao.map

import campus.tech.kakao.map.data.PlaceRemoteDataRepository
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.domain.model.Place
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlaceRemoteDataRepositoryTest {
    private lateinit var placeDao: PlaceDao
    private lateinit var kakaoApi: FakeKakaoApi
    private lateinit var placeRemoteDataRepository: PlaceRemoteDataRepository

    @Before
    fun setup(){
        placeDao = mockk()
        kakaoApi = FakeKakaoApi()
        placeRemoteDataRepository = PlaceRemoteDataRepository(placeDao, kakaoApi)
    }

    @Test
    fun testGetPlaces() = runTest {
        // Given
        val placeName = "장소"
        val page = 1

        // When
        val places: List<Place> = placeRemoteDataRepository.getPlaces(placeName, page)

        // Then
        assertEquals(2, places.size)
        assertEquals("장소 1", places[0].place)
        assertEquals("장소 2", places[1].place)
    }

}