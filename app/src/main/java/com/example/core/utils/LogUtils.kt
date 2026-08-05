package com.example.core.utils

import android.util.Log

object LogUtils {
    fun d(tag: String, message: String) {
        Log.d("MindRest_$tag", message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("MindRest_$tag", message, throwable)
    }

    fun i(tag: String, message: String) {
        Log.i("MindRest_$tag", message)
    }
}
