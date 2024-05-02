package com.abg.liltalemoderation.data.repository

import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.remote.Resource
import com.abg.liltalemoderation.data.remote.exception.UnauthorizedException
import com.abg.liltalemoderation.data.remote.exception.UserNotFoundException
import com.abg.liltalemoderation.model.pojo.ComplaintReport

class ComplaintRepository(private val remoteInstance: RemoteInstance) {

    suspend fun getReports(): Resource<List<ComplaintReport>> {

        val response= remoteInstance.apiComplaintReport().getReports()
        if (response.code() == 401) {
            return Resource.Failure(UnauthorizedException("user unauthorized"))
        } else if (response.code() == 409) {
            return Resource.Failure(UserNotFoundException("user not found"))
        }

        return Resource.Success(response.body()!!)
    }

    // remove post from user account
    // post not ok
    suspend fun removePost(id: Long): Resource<Int> {

        val response = remoteInstance.apiPost().deletePost(id)

        if (response.isSuccessful) {
            return Resource.Success(response.code())
        }

        return Resource.Failure(Exception("fail"))
    }

    suspend fun removeComplaint(complaintReport: ComplaintReport): Resource<Int> {
        // remove complaint
        // post ok
        return Resource.Success(200)
    }
}