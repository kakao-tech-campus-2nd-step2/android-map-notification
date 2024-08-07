package campus.tech.kakao.map.data

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.domain.model.Place
import javax.inject.Inject

class LastVisitedPlaceManager
@Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLastVisitedPlace(place: Place) {
        val editor = sharedPreferences.edit()
        editor.putString("placeName", place.place)
        editor.putString("roadAddressName", place.address)
        editor.putString("categoryName", place.category)
        editor.putString("yPos", place.yPos)
        editor.putString("xPos", place.xPos)
        editor.apply()
    }

    fun getLastVisitedPlace(): Place? {
        val placeName = sharedPreferences.getString("placeName", null)
        val roadAddressName = sharedPreferences.getString("roadAddressName", null)
        val categoryName = sharedPreferences.getString("categoryName", null)
        val yPos = sharedPreferences.getString("yPos", null)
        val xPos = sharedPreferences.getString("xPos", null)

        return if (placeName != null && roadAddressName != null && categoryName != null && yPos != null && xPos != null) {
            Place("", placeName, roadAddressName, categoryName, xPos, yPos)
        } else {
            null
        }
    }
}