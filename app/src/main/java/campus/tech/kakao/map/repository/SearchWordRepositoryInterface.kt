package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord

interface SearchWordRepositoryInterface {
	fun wordFromDocument(document: Document):SearchWord
	suspend fun addWord(word: SearchWord)
	suspend fun deleteWord(word: SearchWord)
	suspend fun loadWord()
}