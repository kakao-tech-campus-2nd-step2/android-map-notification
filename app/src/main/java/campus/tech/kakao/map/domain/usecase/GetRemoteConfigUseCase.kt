package campus.tech.kakao.map.domain.usecase

import androidx.lifecycle.LiveData


interface GetRemoteConfigUseCase {
    suspend operator fun invoke(key: String) : String
}