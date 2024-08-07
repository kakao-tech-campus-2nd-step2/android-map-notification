package campus.tech.kakao.map.model.state

const val SERVICE_STATE_ON_SERVICE = "ON_SERVICE"
const val SERVICE_STATE_ON_PAUSE = "ON_PAUSE"
const val SERVICE_STATE_ON_MAINTENANCE = "ON_MAINTENANCE"

sealed class ServiceState {

    data object NotInitialized : ServiceState()
    data object OnService : ServiceState()
    data class OnPause(val message: String) : ServiceState()
    data class OnMaintenance(val message: String) : ServiceState()
    data object Unknown : ServiceState()

    fun isError() = !(this is NotInitialized || this is OnService)

    companion object {
        fun of(value: String, message: String): ServiceState {
            return when (value) {
                SERVICE_STATE_ON_SERVICE -> OnService
                SERVICE_STATE_ON_PAUSE -> OnPause(message)
                SERVICE_STATE_ON_MAINTENANCE -> OnMaintenance(message)
                else -> Unknown
            }
        }
    }
}
