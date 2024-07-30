package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.ConfigData
import com.google.android.gms.tasks.Task

interface ConfigRepository {
    fun getData(): ConfigData
}