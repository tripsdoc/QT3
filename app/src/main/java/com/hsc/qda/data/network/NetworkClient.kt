package com.hsc.qda.data.network

import com.hsc.qda.data.model.retrieve.RetrieveResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient(private val url: String) {

    companion object {
        const val BASE_URL = "http://192.168.16.16:9220/api/"
        const val BASE_QT3 = "http://192.168.14.147:9220/api/"
    }

    private var mRetrofit: Retrofit? = null
    private var client: OkHttpClient? = null

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getRetrofit(): Retrofit? {
        if (mRetrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            mRetrofit = Retrofit
                    .Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
        }
        return mRetrofit
    }

    fun cancelAllRequest() {
        client?.dispatcher()?.cancelAll()
    }

    fun retrieveTagList(): Call<RetrieveResponse>? {
        return getRetrofit()?.create(APIServices::class.java)?.retrieveTagList()
    }
}