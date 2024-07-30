package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.data.remote_config.ServiceInformation
import campus.tech.kakao.map.domain.repository.RemoteConfigRepository

class GetServiceInformationUseCase(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    suspend operator fun invoke(): ServiceInformation {
        return remoteConfigRepository.getServiceInformation() ?: ServiceInformation(
            "ON_ERROR",
            "Firebase 서버에서 정보를 가져올 수 없습니다.\n나중에 다시 시도해 주세요."
        )
    }
}