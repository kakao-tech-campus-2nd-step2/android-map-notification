package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.document.Document

interface MapPositionRepositoryInterface {

	fun setMapInfo(document: Document)

	fun getMapInfo()
}