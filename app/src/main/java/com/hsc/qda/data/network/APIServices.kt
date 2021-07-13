package com.hsc.qda.data.network

import com.hsc.qda.data.model.retrieve.RetrieveResponse
import retrofit2.Call
import retrofit2.http.GET

interface APIServices {
    @GET("forklift/retrieve")
    fun retrieveTagList(): Call<RetrieveResponse>
}