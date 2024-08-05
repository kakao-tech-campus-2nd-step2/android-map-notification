package campus.tech.kakao.map.view.map

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.kakao.vectormap.MapView

class Map_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapView = MapView(this)
        val mapViewContainer = findViewById<FrameLayout>(R.id.map_view)
        mapViewContainer.addView(mapView)
    }
}
