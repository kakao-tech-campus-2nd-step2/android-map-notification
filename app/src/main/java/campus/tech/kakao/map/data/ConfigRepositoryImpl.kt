package campus.tech.kakao.map.data

import campus.tech.kakao.map.data.datasource.Local.Entity.toVO
import campus.tech.kakao.map.data.datasource.Remote.ConfigService
import campus.tech.kakao.map.domain.ConfigRepository
import campus.tech.kakao.map.domain.vo.Config
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(private val configService: ConfigService) : ConfigRepository{
    override fun getConfig(): Config = configService.getConfig().toVO()
}