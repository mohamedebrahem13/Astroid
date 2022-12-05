package com.example.astroid.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.astroid.asteroidradar.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Retrofit {
    companion object
    {
        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            logging.setLevel((HttpLoggingInterceptor.Level.BODY))
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        }

        val api: RetrofitApi? by lazy {
            retrofit.create(RetrofitApi::class.java)
        }
    }

}