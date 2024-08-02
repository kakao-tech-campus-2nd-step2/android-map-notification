package campus.tech.kakao.map.repository

import android.content.SharedPreferences
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
): MapRepositoryInterface {
    override fun getLastLocation(): Pair<Double, Double>? {
        val x = sharedPreferences.getString("PLACE_X", null)
        val y = sharedPreferences.getString("PLACE_Y", null)
        return if (x != null && y != null) {
            Pair(x.toDouble(), y.toDouble())
        } else {
            null
        }
    }
}