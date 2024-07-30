package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.presentation.view.MapActivity
import campus.tech.kakao.map.presentation.view.SplashActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}
