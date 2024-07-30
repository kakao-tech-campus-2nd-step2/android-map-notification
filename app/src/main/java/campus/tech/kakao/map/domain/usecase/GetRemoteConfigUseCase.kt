package campus.tech.kakao.map.domain.usecase

import androidx.lifecycle.LiveData


interface GetRemoteConfigUseCase {
    operator fun invoke(key: String) : LiveData<String>
}