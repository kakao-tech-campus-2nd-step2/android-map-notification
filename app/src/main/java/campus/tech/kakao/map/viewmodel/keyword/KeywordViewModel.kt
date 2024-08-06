package campus.tech.kakao.map.viewmodel.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.repository.keyword.Keyword
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import campus.tech.kakao.map.viewmodel.OnKeywordItemClickListener
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeywordViewModel  @Inject constructor(private val keywordRepository: KeywordRepository) : ViewModel(),
    OnSearchItemClickListener, OnKeywordItemClickListener {
    private val _keyword = MutableLiveData<List<String>>()
    val keyword: LiveData<List<String>>
        get() = _keyword

    private val _keywordClicked = MutableLiveData<String>()
    val keywordClicked: LiveData<String>
        get() = _keywordClicked

    private suspend fun updateKeywordHistory(keyword: String) {
        keywordRepository.delete(keyword)
        keywordRepository.update(Keyword(keyword = keyword))
        _keyword.value = keywordRepository.read()
    }

    private suspend fun deleteKeywordHistory(keyword: String) {
        keywordRepository.delete(keyword)
        _keyword.value = keywordRepository.read()
    }

    suspend fun readKeywordHistory() {
        _keyword.value = keywordRepository.read()
    }

    fun close() {
        keywordRepository.close()
    }

    override fun onSearchItemClick(localUiModel: LocalUiModel) {
        viewModelScope.launch {
            updateKeywordHistory(localUiModel.place)
        }
    }

    override fun onKeywordItemDeleteClick(keyword: String) {
        viewModelScope.launch {
            deleteKeywordHistory(keyword)
        }
    }

    override fun onKeywordItemClick(keyword: String) {
        _keywordClicked.value = keyword
        viewModelScope.launch {
            updateKeywordHistory(keyword)
        }
    }
}