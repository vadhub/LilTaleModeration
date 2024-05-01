package com.abg.liltalemoderation.data.remote;

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

interface FileService {

    @GET("/api-v1/files/audio/search")
    @Streaming
    suspend fun downloadAudio(
        @Query("id") id: Long,
    ) : Response<ResponseBody>

}
