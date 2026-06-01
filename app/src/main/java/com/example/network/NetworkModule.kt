package com.example.network

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SafeLongAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Long? {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>()
            return null
        }
        return try {
            reader.nextLong()
        } catch (e: Exception) {
            try {
                reader.nextDouble().toLong()
            } catch (e2: Exception) {
                try {
                    reader.nextString().toDouble().toLong()
                } catch (e3: Exception) {
                    null
                }
            }
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: Long?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

object SafeIntAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Int? {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>()
            return null
        }
        return try {
            reader.nextInt()
        } catch (e: Exception) {
            try {
                reader.nextDouble().toInt()
            } catch (e2: Exception) {
                try {
                    reader.nextString().toDouble().toInt()
                } catch (e3: Exception) {
                    null
                }
            }
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: Int?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

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
        .add(SafeLongAdapter)
        .add(SafeIntAdapter)
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
