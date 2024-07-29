package campus.tech.kakao.map.data.datasource.Local.Entity

import campus.tech.kakao.map.domain.vo.Config
import campus.tech.kakao.map.domain.vo.ServiceState
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.DEFAULT_VALUE_FOR_STRING

data class ConfigEntity(
    val serviceState : String,
    val serviceMessage : String
)

fun ConfigEntity.toVO() : Config =
    Config(
        mapServiceState(serviceState),
        if(serviceMessage != DEFAULT_VALUE_FOR_STRING) serviceMessage else "unknown"
    )

fun mapServiceState(serviceState: String): ServiceState =
    when(serviceState) {
        "ON_SERVICE" -> ServiceState.ON_SERVICE
        "OUT_OF_ORDER" -> ServiceState.OUT_OF_ORDER
        else -> ServiceState.ELSE
    }
