package campus.tech.kakao.map.view.adapter

import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord

interface AdapterCallback {
	fun onPlaceClicked(document: Document)
	fun onWordDeleted(searchWord: SearchWord)
	fun onWordSearched(searchWord: SearchWord)
}