package campus.tech.kakao.map.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord
import campus.tech.kakao.map.data.searchWord.SearchWordDao
import javax.inject.Inject

class SearchWordRepository @Inject constructor(
	private val searchWordDao: SearchWordDao
):SearchWordRepositoryInterface {
	private val _wordList = MutableLiveData<List<SearchWord>>()

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
		_wordList.postValue(searchWordDao.getAll())
	}

	fun getWordList(): LiveData<List<SearchWord>> {
		return _wordList
	}
}