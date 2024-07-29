package campus.tech.kakao.map.adapter

import android.view.View
import campus.tech.kakao.map.Room.MapItemEntity

interface ItemClickListener {
    fun onClick(v: View, selectItem: MapItemEntity)
}