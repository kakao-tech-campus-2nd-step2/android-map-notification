package campus.tech.kakao.map.model.splashscreen

data class Service(
    var state: String,
    var msg: String
)

object ServiceRemoteConfig {
    const val SERVICE_STATE = "serviceState"
    const val SERVICE_MESSAGE = "serviceMessage"
}