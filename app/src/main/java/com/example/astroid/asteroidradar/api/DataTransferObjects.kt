package com.example.astroid.asteroidradar.api
import com.example.astroid.asteroidradar.database.DatabaseAsteroid
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<Asteroid>)
// convert from data network model to database model
fun NetworkAsteroidContainer.asDatabaseModel() : Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
