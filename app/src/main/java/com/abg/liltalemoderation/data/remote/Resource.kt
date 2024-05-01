package com.abg.liltalemoderation.data.remote

sealed class Resource<out R> {
    data class Success<out R>(val result: R): Resource<R>()
    data class Failure(val exception: Exception): Resource<Nothing>()
    data object Loading: Resource<Nothing>()
}