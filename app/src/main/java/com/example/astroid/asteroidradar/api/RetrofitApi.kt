package com.example.astroid.asteroidradar.api

import com.example.astroid.asteroidradar.Constants.API_KEY
import com.example.astroid.asteroidradar.Constants.FEED_ENDPOINT
import com.example.astroid.asteroidradar.Constants.PICTURE_OF_THE_DAY
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    // get a steroids from nasa api
    @GET(FEED_ENDPOINT)
    fun getAsteroidListAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Deferred<String>




// get pic of the day from nasa api
    @GET(PICTURE_OF_THE_DAY)
    suspend fun getPictureOfTheDay(
        @Query("api_key") key: String
    ) : PictureOfDay
}