package campus.tech.kakao.map.domain.usecaseImpl

import androidx.lifecycle.LiveData
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository
import campus.tech.kakao.map.domain.usecase.GetRemoteConfigUseCase
import javax.inject.Inject

class GetRemoteConfigUseCaseImpl @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : GetRemoteConfigUseCase {
    override fun invoke(key: String): LiveData<String> {
        return remoteConfigRepository.getConfig(key)
    }
}
