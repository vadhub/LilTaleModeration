package com.abg.liltalemoderation.data.remote.service

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface PostService {

    @DELETE("api-v1/posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): Response<String>

}