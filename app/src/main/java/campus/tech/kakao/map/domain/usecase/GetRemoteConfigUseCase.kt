package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.FirebaseRemoteConfigDomain
import campus.tech.kakao.map.domain.repository.FirebaseRemoteConfigRepository
import javax.inject.Inject

class GetRemoteConfigUseCase @Inject constructor(
    private val repository: FirebaseRemoteConfigRepository,
) {
    operator fun invoke(): FirebaseRemoteConfigDomain {
        return repository.getRemoteConfig()
    }
}
