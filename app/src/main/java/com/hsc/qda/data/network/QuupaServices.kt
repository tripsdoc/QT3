package com.hsc.qda.data.network

import com.hsc.qda.data.model.quupa.position.TagPositionResponse
import com.hsc.qda.data.model.quupa.qda.QdaPositionResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface QuupaServices {
    @GET("getTagData?version=2&humanReadable=true&ignoreUnknownTags=true&maxAge=900000000")
    fun getTagPosition(
        @Query("tag") tags: String
    ): Observable<TagPositionResponse>

    @GET("getTagPosition?ignoreUnknownTags=true")
    fun getQdaPosition(
        @Query("tag") tags: String
    ): Observable<List<QdaPositionResponse>>
}