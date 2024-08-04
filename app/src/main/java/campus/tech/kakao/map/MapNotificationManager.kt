package campus.tech.kakao.map

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.os.Build
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import campus.tech.kakao.map.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


class MapNotificationManager @Inject constructor(
    @ActivityContext private val context: Context,
    private val notificationHelper: AppStatusNotificationHelper
) {
    private val activity: Activity
        get() = context as Activity

    fun askNotificationPermission(requestPermissionLauncher: ActivityResultLauncher<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // FCM SDK (and your app) can post notifications.
                    notificationHelper.showNotification()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    fun shouldShowRequestPermissionRationale(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            false
        }
    }

    fun showNotificationPermissionDialog() {
        AlertDialog.Builder(activity).apply {
            setTitle(activity.getString(R.string.ask_notification_permission_dialog_title))
            setMessage(activity.getString(R.string.ask_notification_permission_dialog_body))
            setPositiveButton(activity.getString(R.string.notification_yes)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
            setNegativeButton(activity.getString(R.string.notification_no)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
}
