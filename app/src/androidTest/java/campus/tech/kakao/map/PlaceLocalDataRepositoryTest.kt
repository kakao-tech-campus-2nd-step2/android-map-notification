package campus.tech.kakao.map

import campus.tech.kakao.map.data.PlaceLocalDataRepository
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.domain.model.Place
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

//장소 데이터 로컬 저장소 테스트
class PlaceLocalDataRepositoryTest {
    private lateinit var placeDao: PlaceDao
    private lateinit var placeLocalDataRepository: PlaceLocalDataRepository

    @Before
    fun setup(){
        placeDao = mockk()
        placeLocalDataRepository = PlaceLocalDataRepository(placeDao)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun testGetPlaces()= runTest {
        //Given
        val placeEntities = listOf(
            PlaceEntity("1", "장소 A", "주소 A", "카테고리 A", "0.0","0.0"),
            PlaceEntity("2", "장소 B", "주소 B", "카테고리 B", "0.0","0.0"),
        )
        coEvery { placeDao.getPlaces("장소") } returns placeEntities

        //When
        val places = placeLocalDataRepository.getPlaces("장소",1)

        //Then
        coVerify { placeDao.getPlaces("장소")}
        assertEquals(2, places.size)
        assertEquals("장소 A", places[0].place)
    }

    @Test
    fun testUpdatePlaces() = runTest{
        //Given
        val places = listOf(
            Place("1", "장소 A", "주소 A", "카테고리 A", "0.0","0.0"),
            Place("2", "장소 B", "주소 B", "카테고리 B", "0.0","0.0"),
        )
        coEvery { placeDao.deleteAllPlaces() } just Runs
        coEvery { placeDao.insertPlaces(any()) } just Runs

        //When
        placeLocalDataRepository.updatePlaces(places)

        //Then
        coVerifySequence {
            placeDao.deleteAllPlaces()
            placeDao.insertPlaces(places.map {
                PlaceEntity(it.id, it.place, it.address, it.category, it.xPos, it.yPos)
            })
        }
    }
}