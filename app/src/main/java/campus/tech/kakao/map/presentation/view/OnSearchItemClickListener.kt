package campus.tech.kakao.map.presentation.view

import campus.tech.kakao.map.domain.model.Location

interface OnSearchItemClickListener {
    fun onSearchItemClick(location: Location)
}