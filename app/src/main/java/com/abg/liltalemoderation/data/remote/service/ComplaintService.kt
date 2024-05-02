package com.abg.liltalemoderation.data.remote.service

import com.abg.liltalemoderation.model.pojo.ComplaintReport
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ComplaintService {

    @GET("/moderation-posts")
    suspend fun getReports(): Response<List<ComplaintReport>>

    @DELETE("api-v1/complaintReports/{id}")
    suspend fun deleteReport(@Path("id") id: Long): Response<String>
}