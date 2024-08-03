package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord
import campus.tech.kakao.map.data.searchWord.SearchWordDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SearchWordRepository @Inject constructor(
	private val searchWordDao: SearchWordDao
):SearchWordRepositoryInterface {
	private val _wordList = MutableStateFlow<List<SearchWord>>(emptyList())
	val wordList: StateFlow<List<SearchWord>> = _wordList.asStateFlow()

	override suspend fun addWord(word: SearchWord) {
		deleteWord(word)
		searchWordDao.insert(word)
		loadWord()
	}

	override fun wordFromDocument(document: Document): SearchWord {
		return SearchWord(document.placeName, document.addressName, document.categoryGroupName)
	}

	override suspend fun deleteWord(word: SearchWord) {
		searchWordDao.delete(word.name, word.address, word.type)
		loadWord()
	}

	override suspend fun loadWord() {
		_wordList.value = searchWordDao.getAll()
	}
}