package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.CoroutineIoDispatcher
import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.data.RemoteConfigRepository
import campus.tech.kakao.map.data.SavedPlace
import campus.tech.kakao.map.data.SavedPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUIState {
    object Loading : SplashUIState()
    object OnService : SplashUIState()
    data class OffService(val message: String) : SplashUIState()
}

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {

    private val _splashUiState = MutableStateFlow<SplashUIState>(SplashUIState.Loading)
    val splashUiState : StateFlow<SplashUIState>
        get() = _splashUiState.asStateFlow()

    fun getRemoteConfigs(){
        viewModelScope.launch {
            remoteConfigRepository.getRemoteConfig().collect{
                if(it.state == "ON_SERVICE"){
                    _splashUiState.value = SplashUIState.OnService
                } else{
                    _splashUiState.value = SplashUIState.OffService(it.message)
                }
            }
        }
    }
}