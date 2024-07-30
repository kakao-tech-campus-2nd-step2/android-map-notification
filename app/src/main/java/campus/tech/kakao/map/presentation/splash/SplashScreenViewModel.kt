package campus.tech.kakao.map.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.RemoteConfigRepository
import campus.tech.kakao.map.domain.model.ConfigData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RemoteConfigState{
    object Loading: RemoteConfigState()
    data class Success(val data: ConfigData) : RemoteConfigState()
    data class Failure(val errorMessage: String?) : RemoteConfigState()
}

@HiltViewModel
class SplashScreenViewModel
@Inject
constructor(private val repository: RemoteConfigRepository): ViewModel() {

    private val _remoteConfigState = MutableLiveData<RemoteConfigState>()
    val remoteConfigState: LiveData<RemoteConfigState> get() = _remoteConfigState

    init {
        fetchAndActiveRemoteConfig()
    }

    private fun fetchAndActiveRemoteConfig(){
        _remoteConfigState.value = RemoteConfigState.Loading

        viewModelScope.launch {
            val fetchedResult = repository.getRemoteResult()

            fetchedResult.fold(
                onSuccess = { data ->
                    _remoteConfigState.value = RemoteConfigState.Success(data)
                },
                onFailure = { exception ->
                    _remoteConfigState.value = RemoteConfigState.Failure(exception.message)
                }
            )
        }
    }
}