package campus.tech.kakao.map.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.view.adapter.DocumentAdapter
import campus.tech.kakao.map.view.adapter.WordAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.mapPosition.MapPositionPreferences
import campus.tech.kakao.map.data.searchWord.SearchWord
import campus.tech.kakao.map.data.remote.RetrofitData
import campus.tech.kakao.map.repository.MapPositionRepository
import campus.tech.kakao.map.repository.SearchWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	application: Application, private val retrofitData: RetrofitData,
	private val searchWordRepository: SearchWordRepository,
	private val mapPositionRepository: MapPositionRepository
) : AndroidViewModel(application) {
	val wordList: LiveData<List<SearchWord>> get() = searchWordRepository.getWordList()

//	private val _documentList = MutableLiveData<List<Document>>()
//	val documentList: LiveData<List<Document>> get() = _documentList
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()

	private val _documentClicked = MutableLiveData<Boolean>()
	val documentClicked: LiveData<Boolean> get() = _documentClicked

	val mapInfo: LiveData<List<String>> get() = mapPositionRepository.getMapInfoList()


	fun addWord(document: Document){
		val word = searchWordRepository.wordFromDocument(document)
		viewModelScope.launch(Dispatchers.IO) {
			searchWordRepository.addWord(word)
		}
	}

	suspend fun deleteWord(word: SearchWord){
		viewModelScope.launch(Dispatchers.IO) {
			searchWordRepository.deleteWord(word)
		}.join()
	}

	fun loadWord(){
		viewModelScope.launch(Dispatchers.IO) {
			searchWordRepository.loadWord()
		}
	}

	fun searchLocalAPI(query: String){
//		viewModelScope.launch(Dispatchers.Main) {
//			retrofitData.searchPlace(query)
//				.collect { documents ->
//					_documentList.value = documents
//					Log.d("testt", documents.toString())
//				}
//
//		}
		retrofitData.searchPlace(query)
	}


	private fun setMapInfo(document: Document){
		mapPositionRepository.setMapInfo(document)
	}

	fun getMapInfo(){
		mapPositionRepository.getMapInfo()
	}

	fun placeClicked(document: Document){
		_documentClicked.value = false
		addWord(document)
		setMapInfo(document)
		_documentClicked.value = true
	}

	fun documentClickedDone(){
		_documentClicked.value = false
	}

	fun doOnTextChanged(query: String, searchBinding: ActivitySearchBinding){
		if (query.isEmpty()){
			searchBinding.noSearchResult.visibility = View.VISIBLE
			searchBinding.searchResultRecyclerView.visibility = View.GONE
		}else{
			searchBinding.noSearchResult.visibility = View.GONE
			searchBinding.searchResultRecyclerView.visibility = View.VISIBLE
			searchLocalAPI(query)
		}
	}

	fun documentListObserved(documents: List<Document>, searchBinding: ActivitySearchBinding, documentAdapter: DocumentAdapter){
		if (documents.isEmpty()){
			searchBinding.noSearchResult.visibility = View.VISIBLE
			searchBinding.searchResultRecyclerView.visibility = View.GONE
		}else{
			searchBinding.noSearchResult.visibility = View.GONE
			searchBinding.searchResultRecyclerView.visibility = View.VISIBLE
			documentAdapter.submitList(documents)
			searchBinding.searchResultRecyclerView.adapter = documentAdapter
		}
	}

	fun wordListObserved(searchWords: List<SearchWord>, searchBinding: ActivitySearchBinding, wordAdapter: WordAdapter){
		if (searchWords.isEmpty()){
			searchBinding.searchWordRecyclerView.visibility = View.GONE
		}
		else{
			searchBinding.searchWordRecyclerView.visibility = View.VISIBLE
			wordAdapter.submitList(searchWords)
			searchBinding.searchWordRecyclerView.adapter = wordAdapter
		}
	}

	companion object{
		const val INIT_LATITUDE = "37.402005"
		const val INIT_LONGITUDE = "127.108621"
		const val LATITUDE = "latitude"
		const val LONGITUDE = "longitude"
		const val PLACE_NAME = "placeName"
		const val ADDRESS_NAME = "addressName"
	}
}