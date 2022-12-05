package com.example.astroid.asteroidradar.api

import com.squareup.moshi.Json
import com.example.astroid.asteroidradar.domain.PictureOfDay
data class PictureOfDay (
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
    )
//convert from network model to domain model
fun com.example.astroid.asteroidradar.api.PictureOfDay.asDomainModel() : PictureOfDay {
    return PictureOfDay(mediaType, title, url)
}