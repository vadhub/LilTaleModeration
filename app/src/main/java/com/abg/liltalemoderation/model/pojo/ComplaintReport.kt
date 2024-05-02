package com.abg.liltalemoderation.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComplaintReport(

    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("post")
    @Expose
    val post: PostResponse,

    @SerializedName("idComplaint")
    @Expose
    val idComplaint: Long,

    @SerializedName("dateCreated")
    @Expose
    val dateCreated: String,

    )
