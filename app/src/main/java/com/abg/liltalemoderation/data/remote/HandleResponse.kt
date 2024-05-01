package com.abg.liltalemoderation.data.remote

interface HandleResponse<T> {
    fun error(e: Exception)
    fun success(t: T)
}