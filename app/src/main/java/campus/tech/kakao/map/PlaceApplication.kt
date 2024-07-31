package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.view.View
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext


@HiltAndroidApp
class PlaceApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKakaoMapSdk()
    }

    private fun initKakaoMapSdk(){
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)
    }
    companion object {
        fun isNetworkActive(@ApplicationContext context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
}
