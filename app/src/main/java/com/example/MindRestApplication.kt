package com.example

import android.app.Application
import com.example.core.utils.LogUtils
import com.example.features.reminder.BedtimeNotificationHelper

class MindRestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.i("Application", "MindRest Application initialized successfully.")
        BedtimeNotificationHelper.createNotificationChannel(this)
    }
}
