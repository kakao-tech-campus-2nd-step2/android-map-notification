package campus.tech.kakao.map.presentation.search

import campus.tech.kakao.map.domain.model.Place

interface SearchActivityListener {
    fun onPlaceClick(place: Place)
    fun onLogDelBtnClick(logId: String)
}
