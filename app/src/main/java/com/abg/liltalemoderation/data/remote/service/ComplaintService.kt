package com.abg.liltalemoderation.data.remote.service

import com.abg.liltalemoderation.model.pojo.ComplaintReport
import retrofit2.Response
import retrofit2.http.GET

interface ComplaintService {

    @GET("/moderation-posts")
    suspend fun getReports(): Response<List<ComplaintReport>>


}