package com.example.network

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    // Note: Change this to your actual Vercel deployment URL when ready.
    // For local testing on an Android emulator: http://10.0.2.2:3000/
    private val BASE_URL: String = try {
        val url = BuildConfig.BASE_URL
        if (url.isNullOrBlank() || url == "BASE_URL") {
            "https://gigueule-buzz.vercel.app/"
        } else {
            url
        }
    } catch (e: Throwable) {
        "https://gigueule-buzz.vercel.app/"
    }

    private val moshi = Moshi.Builder()
        .add(ParticipantAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: BuzzerApiService by lazy {
        retrofit.create(BuzzerApiService::class.java)
    }
}
