package campus.tech.kakao.map.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Place(
    @SerializedName("id") val id: String,
    @SerializedName("place_name") val place: String,
    @SerializedName("address_name") val address: String,
    @SerializedName("category_name")var category: String,
    @SerializedName("x") val xPos: String,
    @SerializedName("y") val yPos: String
): Serializable
