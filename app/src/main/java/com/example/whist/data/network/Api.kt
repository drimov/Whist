package com.example.whist.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val BASE_URL_DEV: String = "http:192.168.1.94"
private const val BASE_URL_PROD: String = "http://dimitrilhopital.com"


private val moshi: Moshi
    get() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

object Network {
    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL_DEV)
        .client(okHttpClient)
        .build()
    val whist: WhistService = retrofit.create(WhistService::class.java)
}