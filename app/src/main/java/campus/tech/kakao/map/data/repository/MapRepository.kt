package campus.tech.kakao.map.data.repository

import android.content.Context
import android.util.Log
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.database.PlacesRoomDB
import campus.tech.kakao.map.data.model.DBPlace
import campus.tech.kakao.map.data.model.DBPlace.Companion.DATABASE_NAME
import campus.tech.kakao.map.data.model.Place
import campus.tech.kakao.map.data.model.RecentSearchWord
import campus.tech.kakao.map.data.network.api.RetrofitService
import campus.tech.kakao.map.data.remote.ConfigService
import campus.tech.kakao.map.data.vo.Config
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepository @Inject constructor(
    private val context: Context,
    private val configService: ConfigService,
    private val retrofitService: RetrofitService,
    private val dataStoreManager: DataStoreManager,
    private val placeDao: PlaceDao,
    private val localRoom: PlacesRoomDB
) : LocalDBRepo {

    var searchHistoryList = ArrayList<RecentSearchWord>()
    lateinit var config: Config

    suspend fun getConfig(): Config {
        config = configService.getConfig()
        return config
    }


    /**
     * 카카오 REST API 관련
     */
    suspend fun searchPlaces(search: String): Result<List<Place>> {
        return try {
            val response = retrofitService.requestPlaces(query = search)
            if (response.documents != null) {
                val responseList = mutableListOf<Place>()
                response.documents.forEach {
                    val category = it.categoryName.split(" \u003e ").last()
                    responseList.add(Place(it.placeName, it.addressName, category, it.x, it.y))
                }
                Log.d("search", "search3: ${Thread.currentThread().name}")
                Result.success(responseList)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Local DB 관련 - Room
     */
    private fun dbFileExists(dbName: String): Boolean {
        val dbFile = context.getDatabasePath("$dbName")
        return dbFile.exists()
    }

    suspend fun insertRoomInitialData() {
        if (!dbFileExists(DATABASE_NAME)) {
            val places = mutableListOf<DBPlace>()
            for (i in 1..30) {
                val cafe = DBPlace(name = "공원$i", address = "서울 성동구 성수동 $i", category = "카페")
                val pharmacy =
                    DBPlace(name = "병원$i", address = "서울 강남구 대치동 $i", category = "약국")
                places.add(cafe)
                places.add(pharmacy)
            }
            localRoom.placeDao().insertAll(*places.toTypedArray())
            Log.d("search2", "initialData: ${Thread.currentThread().name}")
        }
    }

    private fun DBPlace.toPlace(): Place {
        return Place(
            name = this.name,
            address = this.address,
            category = this.category,
            longitude = "",
            latitude = ""
        )
    }

    suspend fun searchRoomPlaces(search: String, onPlaceResponse: (List<Place>) -> Unit) {
        val filtered: List<Place> = getAllPlaces()
            .filter { it.name.contains(search, ignoreCase = true) }
            .map { dbPlace -> dbPlace.toPlace() }
        Log.d("Thread", "${Thread.currentThread().name}")
        onPlaceResponse(filtered)
    }

    override suspend fun getAllPlaces(): List<DBPlace> = placeDao.getAllPlaces()

    override suspend fun insertAll(vararg dbPlace: DBPlace) = placeDao.insertAll(*dbPlace)

    override suspend fun delete(dbPlace: DBPlace) = placeDao.delete(dbPlace)


    /**
     * datastore 관련
     */
    fun getSearchHistory(): ArrayList<RecentSearchWord> {
        return searchHistoryList
    }

    fun searchHistoryContains(itemName: String): Int {
        return searchHistoryList.indexOfFirst { it.word == itemName }
    }

    suspend fun moveSearchToLast(idx: Int, search: String) {
        if (idx == searchHistoryList.size - 1) return
        searchHistoryList.removeAt(idx)
        searchHistoryList.add(RecentSearchWord(search))
        saveSearchHistory()
    }

    suspend fun addSearchHistory(search: String) {
        withContext(Dispatchers.IO) {
            searchHistoryList.add(RecentSearchWord(search))
            saveSearchHistory()
        }
    }

    suspend fun delSearchHistory(idx: Int) {
        withContext(Dispatchers.IO) {
            searchHistoryList.removeAt(idx)
            saveSearchHistory()
        }
    }

    private suspend fun saveSearchHistory() {
        dataStoreManager.saveSearchHistory(searchHistoryList)
    }

    suspend fun setPrefs() {
        searchHistoryList = dataStoreManager.getSearchHistory()
    }

    suspend fun getLastPos(): LatLng? {
        return dataStoreManager.getLastPos()
    }

    suspend fun savePos(latLng: LatLng) {
        return dataStoreManager.savePos(latLng)
    }

}