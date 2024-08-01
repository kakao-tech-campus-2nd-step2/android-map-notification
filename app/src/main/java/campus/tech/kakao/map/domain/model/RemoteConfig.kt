package campus.tech.kakao.map.domain.model

class RemoteConfig(
    val serviceState: String = "",
    val serviceMessage: String = ""
) {
    companion object {
        const val KEY_SERVICE_STATE = "serviceState"
        const val KEY_SERVICE_MESSAGE = "serviceMessage"
    }
}