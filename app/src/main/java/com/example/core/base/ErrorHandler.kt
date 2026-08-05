package com.example.core.base

import com.example.core.common.Resource

interface ErrorHandler {
    fun getErrorMessage(throwable: Throwable): Resource
}

class DefaultErrorHandler : ErrorHandler {
    override fun getErrorMessage(throwable: Throwable): Resource {
        return Resource.DynamicString(throwable.localizedMessage ?: "An unexpected error occurred.")
    }
}
