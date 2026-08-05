package com.example.features.reminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class BedtimeNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context)
    }

    companion object {
        const val CHANNEL_ID = "bedtime_reminder_channel"
        const val NOTIFICATION_ID = 1001

        fun showNotification(context: Context) {
            val contentIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("🌙 Time to Wind Down")
                .setContentText("Your ideal bedtime is in 30 minutes (11:15 PM). Start your relaxation routine now!")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Your ideal bedtime is in 30 minutes (11:15 PM). Turn off screens, lower the lights, and try a quick guided meditation or breathing exercise to maximize deep sleep.")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }
}
