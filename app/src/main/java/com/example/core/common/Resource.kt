package com.example.core.common

import android.content.Context
import androidx.annotation.StringRes

sealed interface Resource {
    data class DynamicString(val value: String) : Resource
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : Resource {
        override fun asString(context: Context): String {
            return context.getString(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> asString(context)
        }
    }
}
