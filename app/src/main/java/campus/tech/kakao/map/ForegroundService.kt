package campus.tech.kakao.map

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundService : Service() {

    @Inject
    lateinit var notificationHelper: AppStatusNotificationHelper

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification = notificationHelper.createNotification(
            "포그라운드 서비스",
            "앱이 포그라운드에서 실행 중입니다."
        )

        startForeground(AppStatusNotificationHelper.NOTIFICATION_ID, notification)

        // 서비스가 강제 종료될 경우 재시작하지 않음
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
