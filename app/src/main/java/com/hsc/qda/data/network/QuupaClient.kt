package com.hsc.qda.data.network

import com.hsc.qda.data.model.quupa.position.TagPositionResponse
import com.hsc.qda.data.model.quupa.qda.QdaPositionResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class QuupaClient(private val url: String) {
    companion object {
        const val QPA_URL = "http://192.168.1.20:9132/qpe/"
        const val QDA_URL = "http://192.168.14.147:9440/qda/"
    }

    private var retrofit: Retrofit? = null

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            retrofit = Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        }
        return retrofit
    }

    fun getTagPosition(tags: String): Observable<TagPositionResponse>? {
        return getRetrofit()?.create(QuupaServices::class.java)?.getTagPosition(tags)
    }

    fun getQdaPosition(tags: String): Observable<List<QdaPositionResponse>>? {
        return getRetrofit()?.create(QuupaServices::class.java)?.getQdaPosition(tags)
    }
}