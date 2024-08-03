package campus.tech.kakao.map.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.data.repository.log.LogRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logRepository: LogRepositoryInterface
): ViewModel() {
    private var _logList = MutableLiveData<List<Place>>()
    val logList: LiveData<List<Place>> get() = _logList

    private var _tabViewVisible = MutableLiveData<Boolean>()
    val tabViewVisible: LiveData<Boolean> get() = _tabViewVisible

    init {
        initLogData()
    }

    private fun initLogData(){
        viewModelScope.launch {
            initLogList()
            updateLogViewVisible()
        }
    }

    private suspend fun initLogList(){
        _logList.value = logRepository.getAllLogs()
    }

    private suspend fun updateLogViewVisible(){
        _tabViewVisible.value = logRepository.haveAnyLog()
    }

    fun deleteLog(item: Place){
        viewModelScope.launch {
            logRepository.deleteLog(item)
            _logList.value = logRepository.getAllLogs()
            updateLogViewVisible()
        }
    }

    fun insertLog(item: Place) {
        viewModelScope.launch {
            logRepository.insertLog(item)
            _logList.value = logRepository.getAllLogs()
            updateLogViewVisible()
        }
    }
}