package com.abg.liltalemoderation.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComplaintReport(

    @SerializedName("post")
    @Expose
    val post: PostResponse,

    @SerializedName("idComplaint")
    @Expose
    val idComplaint: Int,

    @SerializedName("dateCreated")
    @Expose
    val dateCreated: String,

    ) {
    companion object {
        fun empty(): ComplaintReport =ComplaintReport(PostResponse.empty(), -1, "")
    }
}
