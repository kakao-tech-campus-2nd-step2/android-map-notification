package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.FirebaseRemoteConfigRepository
import javax.inject.Inject

class FetchRemoteConfigUseCase @Inject constructor(
    private val repository: FirebaseRemoteConfigRepository,
) {

    suspend operator fun invoke(): Boolean {
        return repository.fetchRemoteConfig()
    }
}
