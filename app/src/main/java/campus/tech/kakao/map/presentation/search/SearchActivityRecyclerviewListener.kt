package campus.tech.kakao.map.presentation.search

import campus.tech.kakao.map.domain.model.Place

interface SearchActivityRecyclerviewListener {
    fun onPlaceClick(place: Place)
    fun onLogDelBtnClick(logId: String)
}
