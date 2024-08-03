package campus.tech.kakao.map.data.repository.map

interface MapRepositoryInterface {
    fun getLastLocation(): Pair<Double, Double>?
}