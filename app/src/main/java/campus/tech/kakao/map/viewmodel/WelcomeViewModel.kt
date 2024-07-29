package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.repository.WelcomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val welcomeRepository: WelcomeRepository
): ViewModel() {
    private val _serviceState = MutableLiveData<String>()
    val serviceState: LiveData<String> get() = _serviceState

    private val _serviceMsg = MutableLiveData<String>()
    val serviceMsg: LiveData<String> get() = _serviceMsg
}