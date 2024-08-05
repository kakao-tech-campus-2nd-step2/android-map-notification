package campus.tech.kakao.map.ui

import campus.tech.kakao.map.domain.Place

data class PlaceUiModel(
    val placeList: List<Place>,
    val searchList: List<Place>,
    val isPlaceListVisible: Boolean,
    val isSearchListVisible: Boolean
)
