package campus.tech.kakao.map.presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import campus.tech.kakao.map.base.BaseNotificationService

class SystemBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationService = BaseNotificationService(context)
            when (intent?.action) {
                Intent.ACTION_SCREEN_ON -> notificationService.notifyFromBroadcastReceiver("화면 켜짐")
                Intent.ACTION_BOOT_COMPLETED -> notificationService.notifyFromBroadcastReceiver("부팅")
                Intent.ACTION_BATTERY_CHANGED -> notificationService.notifyFromBroadcastReceiver("배터리 변화")
                else -> notificationService.notifyFromBroadcastReceiver(intent?.action.toString())
            }
        }
    }
}