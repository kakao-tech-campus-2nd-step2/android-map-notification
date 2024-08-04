package campus.tech.kakao.map

import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SearchWordRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FunTest{
	private val searchWordRepository= mockk<SearchWordRepository>()
	private val placeRepository= mockk<PlaceRepository>()
	private val documentList = MutableStateFlow<List<Document>>(listOf(
		Document(
			"이안아파트", "아파트",
			"남양주", "10",
			"10")
	))
	private val wordList = MutableLiveData<List<SearchWord>>(listOf())

	@Test
	fun 검색어를_입력하면_검색_결과_표시(){
		val query = "이안아파트"
		every { placeRepository.searchPlace(query) } returns Unit
		every { placeRepository.documentList } returns documentList
		placeRepository.searchPlace(query)
		val actualQueryResult = placeRepository.documentList.value
		assert(actualQueryResult.any { it.placeName == query })
	}


	@Test
	fun 검색어_저장_되는지_확인(){
		val query = SearchWord(
			"이안아파트", "남양주", "아파트")

		val expectedResult = SearchWord(
			"이안아파트", "남양주", "아파트")

		coEvery { searchWordRepository.addWord(any()) } returns insert(query)

		CoroutineScope(Dispatchers.IO).launch {
			searchWordRepository.addWord(query)
		}
		assert(wordList.value?.get(0) == expectedResult)
	}

	@Test
	fun 검색어_삭제_되는지_확인(){
		val word = SearchWord(
			"이안아파트", "남양주", "아파트")
		coEvery { searchWordRepository.deleteWord(any()) } returns delete()
		coEvery { searchWordRepository.loadWord()} returns Unit
		CoroutineScope(Dispatchers.IO).launch {
			searchWordRepository.deleteWord(word)
		}
		assert(wordList.value?.isEmpty()!!)
	}


	private fun insert(searchWord: SearchWord){
		wordList.value = listOf(searchWord)
	}
	private fun delete(){
		wordList.value = listOf()
	}

}