package campus.tech.kakao.map.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord
import campus.tech.kakao.map.repository.MapPositionRepository
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SearchWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	application: Application, private val placeRepository: PlaceRepository,
	private val searchWordRepository: SearchWordRepository,
	private val mapPositionRepository: MapPositionRepository
) : AndroidViewModel(application) {
	val wordList: LiveData<List<SearchWord>> get() = searchWordRepository.getWordList()

	var documentList: LiveData<List<Document>>

	private val _documentClicked = MutableLiveData<Boolean>()
	val documentClicked: LiveData<Boolean> get() = _documentClicked

	val mapInfo: LiveData<List<String>> get() = mapPositionRepository.mapInfoList.asLiveData()

	init {
		documentList = placeRepository.searchPlace("")
		loadWord()
	}

	private fun addWord(document: Document){
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

	private fun loadWord(){
		viewModelScope.launch(Dispatchers.IO) {
			searchWordRepository.loadWord()
		}
	}

	fun searchLocalAPI(query: String){
		documentList = placeRepository.searchPlace(query)
	}

	fun getMapInfo(){
		mapPositionRepository.getMapInfo()
	}

	fun placeClicked(document: Document){
		_documentClicked.value = false
		addWord(document)
		mapPositionRepository.setMapInfo(document)
		_documentClicked.value = true
	}

	fun documentClickedDone(){
		_documentClicked.value = false
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