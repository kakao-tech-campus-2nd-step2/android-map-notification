package campus.tech.kakao.map.base

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presenter.view.MapActivity

@BindingAdapter("visibleGone")
fun setVisibleGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("errorText")
fun setErrorTest(view: TextView, type: ErrorEnum){
    view.text = when(type) {
        ErrorEnum.MAP_LOAD_ERROR -> view.resources.getString(R.string.map_error)
        else -> view.resources.getString(R.string.else_error)
    }
}
